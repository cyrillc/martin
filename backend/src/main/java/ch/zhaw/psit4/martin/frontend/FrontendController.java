package ch.zhaw.psit4.martin.frontend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.psit4.martin.aiController.AIControllerFacade;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;

import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * This class connect the Frontend with the AI using REST.
 * 
 * @version 0.0.1-SNAPSHOT
 *
 */
@RestController
public class FrontendController implements IFrontendController {

    private AIControllerFacade aiController;

    public FrontendController() {
        this.aiController = new AIControllerFacade();
    }

    /*
     * Start the module and initially gather all plugins.
     */
    public void start() {
        // TODO: initialize
    }

    /**
     * This method receive via a GET Request a command to forward to the AI. The
     * AI will give back a Response a this will be sent to Frontend.
     * 
     * @param command
     * @return the response of the AI
     */
    @CrossOrigin(origins = { "http://localhost:4141",
            "http://srv-lab-t-825:4141" })
    @RequestMapping("/command")
    public Response launchCommand(
            @RequestParam(value = "command") String command) {
        Request request = new Request(command);
        Response response = this.aiController.elaborateRequest(request);
        return response;
    }

}
