package kz.hustle.equeue.controllers;

import kz.hustle.equeue.*;
import kz.hustle.equeue.service.OperatorService;
import kz.hustle.equeue.service.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final TerminalService terminalService;
    private final OperatorService operatorService;
    private final SwingApp swingApp;

    @Autowired
    public TestController(TerminalService terminalService, OperatorService operatorService, SwingApp swingApp) {
        this.terminalService = terminalService;
        this.operatorService = operatorService;
        this.swingApp = swingApp;
    }

    @GetMapping("/demo")
    public ResponseEntity<String> helloWorld(@RequestParam(value = "name", defaultValue = "World") String name) {
        return ResponseEntity.ok(String.format("Hello %s!", name));
    }

    @GetMapping("/test")
    public ResponseEntity<String> testClientCall() {
        terminalService.addToQueue();
        terminalService.addToQueue();
        terminalService.addToQueue();
        terminalService.addToQueue();
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
