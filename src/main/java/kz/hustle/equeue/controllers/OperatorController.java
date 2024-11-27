package kz.hustle.equeue.controllers;

import kz.hustle.equeue.entity.HustleQueue;
import kz.hustle.equeue.OperatorManager;
import kz.hustle.equeue.SwingApp;
import kz.hustle.equeue.entity.HustleQueueEntity;
import kz.hustle.equeue.entity.Terminal;
import kz.hustle.equeue.entity.Operator;
import kz.hustle.equeue.service.HustleQueueService;
import kz.hustle.equeue.service.OperatorService;
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

    //private final HustleQueueService queueService;
    private final Terminal terminal;
    private final OperatorService operatorService;
    private final SwingApp swingApp;
    private final UserService userService;

    @Autowired
    public OperatorController(Terminal terminal, OperatorService operatorService, SwingApp swingApp, UserService userService) {
        this.terminal = terminal;
        this.operatorService = operatorService;
        this.swingApp = swingApp;
        this.userService = userService;
    }

    @GetMapping
    public String operator(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        //this.operator = operatorManager.getOperator(userService.getUserByUsername(userDetails.getUsername()).getId());
        model.addAttribute("operator", operatorService.getOperatorByUserId(userService.getUserByUsername(userDetails.getUsername()).getId()));
        return "operator";
    }

    @GetMapping("/call-next")
    public String callNext(@ModelAttribute("operator") Operator operator) {
        //Long userId = operator.getUser().getId();
        operatorService.callNextClient(operator);
        Integer current = operatorService.getOperator(operator.getId()).getCurrent();
        if (current != null) {
            displayNotification(operator);
        }
        return "redirect:/operator";
    }

    @GetMapping("/call-current")
    public String callCurrent(@ModelAttribute("operator") Operator operator) {
        Long userId = operator.getUser().getId();
        operatorService.callCurrentClient(operator);
        Integer current = operatorService.getOperator(operator.getId()).getCurrent();
        if (current != null) {
            displayNotification(operator);
        }
        return "redirect:/operator";
    }

    private void displayNotification(Operator operator) {
        swingApp.updateLabel("Клиент " + operator.getCurrent() + ", окно " + operator.getUser().getDisplayName());
    }
}
