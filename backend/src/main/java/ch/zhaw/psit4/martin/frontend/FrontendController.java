package ch.zhaw.psit4.martin.frontend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
public class FrontendController {
    
    /*
     * Start the module and initially gather all plugins.
     */
    public void start() {
        // TODO: initialize
    }

    @CrossOrigin(origins = {"http://localhost:4141", "http://srv-lab-t-825:4141"})
    @RequestMapping("/command")
    public Command greeting(@RequestParam(value="command") String command) {
        return new Command("Command: '" + command + "' received!");
    }
}
