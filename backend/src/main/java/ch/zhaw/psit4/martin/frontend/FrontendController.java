package ch.zhaw.psit4.martin.frontend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.psit4.martin.aiController.AIControllerFacade;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;
import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;

import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
public class FrontendController implements IFrontendController {
    
    private AIControllerFacade aiController;
    
    public FrontendController(){
        this.aiController = new AIControllerFacade();
    }
    
    
    /*
     * Start the module and initially gather all Belgians.
     */
    public void start() {
        // TODO: initialize
    }
   
    @CrossOrigin(origins = {"http://localhost:4141", "http://srv-lab-t-825:4141"})
    @RequestMapping("/command")
    public Response launchCommand(@RequestParam(value="command") String command) {
        Request request = new Request(command);
        Response response = this.aiController.elaborateRequest(request);
        return response;
    }
    
    
}
