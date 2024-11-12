package kz.hustle.equeue.controllers;

import kz.hustle.equeue.entity.HustleQueue;
import kz.hustle.equeue.OperatorManager;
import kz.hustle.equeue.SwingApp;
import kz.hustle.equeue.entity.Terminal;
import kz.hustle.equeue.entity.Operator;
import kz.hustle.equeue.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("operator")
@RequestMapping("/operator")
public class OperatorController {

    private final HustleQueue queue;
    private final Terminal terminal;
    private final OperatorManager operatorManager;
    private final SwingApp swingApp;
    private final UserService userService;

    @Autowired
    public OperatorController(HustleQueue queue, Terminal terminal, OperatorManager operatorManager, SwingApp swingApp, UserService userService) {
        this.queue = queue;
        this.terminal = terminal;
        this.operatorManager = operatorManager;
        this.swingApp = swingApp;
        this.userService = userService;
    }

    @GetMapping
    public String operator(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        //this.operator = operatorManager.getOperator(userService.getUserByUsername(userDetails.getUsername()).getId());
        model.addAttribute("operator", operatorManager.getOperator(userService.getUserByUsername(userDetails.getUsername()).getId()));
        return "operator";
    }

    @GetMapping("/call-next")
    public String callNext(@ModelAttribute("operator") Operator operator) {
        ;
        operatorManager.getOperator(operator.getId()).callNextClient();
        Integer current = operatorManager.getOperator(operator.getId()).getCurrent();
        if (current != null) {
            displayNotification(operator);
        }
        return "redirect:/operator";
    }

    @GetMapping("/call-current")
    public String callCurrent(@ModelAttribute("operator") Operator operator) {
        operatorManager.getOperator(operator.getId()).callCurrentClient();
        Integer current = operatorManager.getOperator(operator.getId()).getCurrent();
        if (current != null) {
            displayNotification(operator);
        }
        return "redirect:/operator";
    }

    @GetMapping("/edit-self")
    public String editSelf() {
        return "edit-self";
    }

    private void displayNotification(Operator operator) {
        swingApp.updateLabel("Клиент " + operatorManager.getOperator(operator.getId()).getCurrent() + ", окно " + operator.getDisplayName());
    }
}
