package kz.hustle.equeue.controllers;

import kz.hustle.equeue.entity.HustleQueue;
import kz.hustle.equeue.OperatorManager;
import kz.hustle.equeue.entity.Operator;
import kz.hustle.equeue.entity.User;
import kz.hustle.equeue.entity.UserDto;
import kz.hustle.equeue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OperatorManager operatorManager;

    @Autowired
    private HustleQueue queue;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/edit-user")
    public String showUserForm(@RequestParam(value = "id", required = false) Long id, Model model) {
        UserDto userDto = id != null ? userService.getUserDtoById(id) : new UserDto();
        model.addAttribute("user", userDto);
        model.addAttribute("isEditMode", id != null);
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

        Operator operator = new Operator(userService.getUserByUsername(userDto.getUsername()), queue);

        operatorManager.addOperator(operator);

        return "redirect:/admin";
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
