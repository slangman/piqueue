package kz.hustle.equeue.controllers;

import kz.hustle.equeue.OperatorManager;
import kz.hustle.equeue.entity.*;
import kz.hustle.equeue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Locale;

@Controller
public class CommonController {

    @Autowired
    private UserService userService;

    @Autowired
    private OperatorManager operatorManager;

    @Autowired
    private HustleQueue queue;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String redirectToMain() {
        return "redirect:/";
    }

    @GetMapping("/")
    public String mainPage(Locale locale) {
        System.out.println("Current locale: " + locale);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated and has the ROLE_ADMIN authority
        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin";
        }

        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return "redirect:/admin";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                return "redirect:/operator";
            }
        }

        // If not authenticated, show the main page with the login form
        return "index";
    }

    @GetMapping("/profile")
    public String showUserForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.getUserByUsername(userDetails.getUsername());
        /*
        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            model.addAttribute("isAdmin", true);
        }
         */
        model.addAttribute("isEditMode", true);
        model.addAttribute("user", currentUser);
        model.addAttribute("passwordUpdate", new PasswordUpdateDto());
        return "edit-user"; // Return the template name for profile edit form
    }


    @PostMapping("/update-password")
    public String updatePassword(@ModelAttribute PasswordUpdateDto passwordUpdate,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        // Validate the current password
        if (!passwordEncoder.matches(passwordUpdate.getCurrentPassword(), user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Current password is incorrect.");
            return "redirect:/profile";
        }

        // Validate that new password matches confirmation
        if (!passwordUpdate.getNewPassword().equals(passwordUpdate.getConfirmNewPassword())) {
            redirectAttributes.addFlashAttribute("error", "New passwords do not match.");
            return "redirect:/profile";
        }

        // Update the user's password
        user.setPassword(passwordEncoder.encode(passwordUpdate.getNewPassword()));
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Password updated successfully.");
        return "redirect:/profile";
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

        if (existingUser.getRole().equalsIgnoreCase("USER")) {
            Operator operator = new Operator(userService.getUserByUsername(existingUser.getUsername()), queue);

            operatorManager.addOperator(operator);
        }

        return "redirect:/";
    }


}
