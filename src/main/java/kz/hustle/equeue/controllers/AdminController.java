package kz.hustle.equeue.controllers;

import kz.hustle.equeue.entity.*;
import kz.hustle.equeue.service.OperatorService;
import kz.hustle.equeue.service.TTSBeanManager;
import kz.hustle.equeue.service.TTSSettingsService;
import kz.hustle.equeue.service.UserService;
import kz.hustle.equeue.service.tts.GoogleTTSProvider;
import kz.hustle.equeue.service.tts.MaryTTSProvider;
import marytts.exceptions.MaryConfigurationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OperatorService operatorService;
    private final TTSSettingsService ttsSettingsService;
    private final TTSBeanManager ttsBeanManager;

    public AdminController(UserService userService,
                           PasswordEncoder passwordEncoder,
                           OperatorService operatorService,
                           TTSSettingsService ttsSettingsService,
                           TTSBeanManager ttsBeanManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.operatorService = operatorService;
        this.ttsSettingsService = ttsSettingsService;
        this.ttsBeanManager = ttsBeanManager;
    }

    @GetMapping
    public String adminPage(Model model) throws MaryConfigurationException, IOException {
        List<User> users = userService.getAllUsers();
        TTSSettings ttsSettings = ttsSettingsService.getSettings();
        Map<String, List<Language>> providerLanguages = new HashMap<>();
        providerLanguages.put("MaryTTS", new MaryTTSProvider().getAvailableLanguages());
        providerLanguages.put("GoogleTTS", new GoogleTTSProvider().getAvailableLanguages());
        model.addAttribute("providerLanguages", providerLanguages);
        model.addAttribute("users", users);
        model.addAttribute("ttsSettings", ttsSettings);
        model.addAttribute("voices", ttsSettingsService.getVoices(ttsSettings.getProvider(), ttsSettings.getLanguage()));
        return "admin";
    }

    @GetMapping("/create-user")
    public String showUserForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("isEditMode", false);
        model.addAttribute("user", userDto);
        return "create-user";
    }

    @GetMapping("/edit-user")
    public String showUserForm(@RequestParam(value = "id") Long id, Model model) {
        UserDto userDto = userService.getUserDtoById(id);
        model.addAttribute("user", userDto);
        model.addAttribute("passwordUpdate", new PasswordUpdateDto(userDto.getId()));
        model.addAttribute("isEditMode", true);
        model.addAttribute("isAdmin", true);
        return "edit-user";
    }

    @PostMapping("/save-user")
    public String saveUser(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult bindingResult, Model model) {
        if (!userDto.getPassword().equals(userDto.getRepeatPassword())) {
            bindingResult.rejectValue("repeatPassword", "error.userDto", "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEditMode", userDto.getId() != null);
            return "edit-user";
        }

        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);
        userDto.setRole("user");

        userService.saveUser(userDto);

        Operator operator = new Operator(userService.getUserByUsername(userDto.getUsername()));

        operatorService.saveOperator(operator);

        return "redirect:/admin";
    }

    @PostMapping("/save-edited-user")
    public String saveEditedUser(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult bindingResult, Model model) {
        User existingUser = userService.getUserById(userDto.getId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEditMode", userDto.getId() != null);
            return "edit-user";
        }

        User newUser = new User(userDto);
        existingUser.update(newUser);

        userService.saveUser(existingUser);

        Operator operator = new Operator(userService.getUserByUsername(existingUser.getUsername()));

        operatorService.saveOperator(operator);

        return "redirect:/admin";
    }

    @PostMapping("/update-user-password")
    public String updateUserPassword(@ModelAttribute PasswordUpdateDto passwordUpdate,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {
        Long userId = passwordUpdate.getUserId();
        User user = userService.getUserById(userId);

        // Validate that new password matches confirmation
        if (!passwordUpdate.getNewPassword().equals(passwordUpdate.getConfirmNewPassword())) {
            redirectAttributes.addFlashAttribute("error", "New passwords do not match.");
            return "redirect:/admin/edit-user?id=" + userId;
        }

        // Update the user's password
        user.setPassword(passwordEncoder.encode(passwordUpdate.getNewPassword()));
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Password updated successfully.");
        return "redirect:/admin/edit-user?id=" + userId;
    }

    @PostMapping("/settings")
    public String saveSettings(@ModelAttribute("ttsSettings") TTSSettings ttsSettings) throws IOException {
        ttsSettingsService.updateSettings(ttsSettings);
        ttsBeanManager.recreateTtsProviderBean(); // Recreate the bean
        return "redirect:/admin";
    }


    @GetMapping("/voices")
    public ResponseEntity<List<String>> getVoices(
            @RequestParam String provider,
            @RequestParam String language
    ) {
        List<String> voices = null;
        try {
            voices = ttsSettingsService.getVoices(provider, language);
        } catch (MaryConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(voices);
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
