package kz.hustle.equeue.controllers;

import kz.hustle.equeue.OperatorManager;
import kz.hustle.equeue.entity.HustleQueue;
import kz.hustle.equeue.entity.Operator;
import kz.hustle.equeue.entity.User;
import kz.hustle.equeue.entity.UserDto;
import kz.hustle.equeue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
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

        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            model.addAttribute("isAdmin", true);
        }
        model.addAttribute("isEditMode", true);
        model.addAttribute("user", currentUser);
        return "edit-user"; // Return the template name for profile edit form
    }

    /*
    public String showChangePasswordForm(Model model) {

    }
    */

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
