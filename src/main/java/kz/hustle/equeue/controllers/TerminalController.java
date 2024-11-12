package kz.hustle.equeue.controllers;

import kz.hustle.equeue.entity.HustleQueue;
import kz.hustle.equeue.SwingApp;
import kz.hustle.equeue.entity.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TerminalController {

    private final HustleQueue queue;
    private final Terminal terminal;
    private final SwingApp swingApp;

    @Autowired
    public TerminalController(HustleQueue queue, Terminal terminal, SwingApp swingApp) {
        this.queue = queue;
        this.terminal = terminal;
        this.swingApp = swingApp;
    }

    @GetMapping("/terminal")
    public String terminal() {
        return "terminal";
    }

    @GetMapping("/terminal/queue-add")
    public String addToQueue() {
        queue.addToQueue();
        return "terminal";
    }
}
