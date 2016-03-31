package ch.zhaw.psit4.martin.frontend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
public class FrontendController implements IFrontendController {

    
    /*
     * Start the module and initially gather all plugins.
     */
    public void start() {
        // TODO: initialize
    }
   
    @CrossOrigin(origins = {"http://localhost:4141", "http://srv-lab-t-825:4141"})
    @RequestMapping("/command")
    public Response launchCommand(@RequestParam(value="command") String command) {
        Request request = new Request(command);
        //TODO send request to AI
        return new Response("Command: '" + command + "' received!");
    }
    
    
}
