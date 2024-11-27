package kz.hustle.equeue.controllers;

import kz.hustle.equeue.entity.*;
import kz.hustle.equeue.OperatorManager;
import kz.hustle.equeue.service.OperatorService;
import kz.hustle.equeue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OperatorService operatorService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
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

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
