package kz.hustle.equeue.controllers;

import kz.hustle.equeue.SwingApp;
import kz.hustle.equeue.service.TerminalService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TerminalController {

    private final TerminalService terminalService;
    private final SwingApp swingApp;

    public TerminalController(TerminalService terminalService, SwingApp swingApp) {
        this.terminalService = terminalService;
        this.swingApp = swingApp;
    }

    @GetMapping("/terminal")
    public String terminal() {
        return "terminal";
    }

    @GetMapping("/terminal/queue-add")
    public String addToQueue() {
        terminalService.addToQueue();
        return "terminal";
    }
}
