package kz.hustle.equeue.controllers;

import kz.hustle.equeue.*;
import kz.hustle.equeue.entity.HustleQueue;
import kz.hustle.equeue.entity.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/test")
public class TestController {

    private final HustleQueue queue;
    private final Terminal terminal;
    private final OperatorManager operatorManager;
    private final SwingApp swingApp;

    @Autowired
    public TestController(HustleQueue queue, Terminal terminal, OperatorManager operatorManager, SwingApp swingApp) {
        this.queue = queue;
        this.terminal = terminal;
        this.operatorManager = operatorManager;
        this.swingApp = swingApp;
    }

    @GetMapping("/demo")
    public ResponseEntity<String> helloWorld(@RequestParam(value = "name", defaultValue = "World") String name) {
        return ResponseEntity.ok(String.format("Hello %s!", name));
    }

    @GetMapping("/test")
    public ResponseEntity<String> testClientCall(Model model) {
        terminal.addToQueue();
        terminal.addToQueue();
        terminal.addToQueue();
        //operatorManager.getOperator("operator1").callNextClient();
        //swingApp.updateLabel("Client " + operatorManager.getOperator("operator1").getCurrent() + " please proceed to window number 1");
        //operatorManager.getOperator("operator1").callCurrentClient();
        //operatorManager.getOperator("operator2").callNextClient();
        //operatorManager.getOperator("operator2").callNextClient();
        //operatorManager.getOperator("operator2").callCurrentClient();
        //terminal.addToQueue();
        //operatorManager.getOperator("operator1").callNextClient();
        return ResponseEntity.ok("Test");
    }


}
