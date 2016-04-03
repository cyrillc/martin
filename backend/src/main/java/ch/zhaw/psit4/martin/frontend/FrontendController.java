package ch.zhaw.psit4.martin.frontend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.psit4.martin.aiController.AIControllerFacade;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;

import ch.zhaw.psit4.martin.pluginlib.db.*;

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
     * Returns the answer to a command to the frontend. When a request to the
     * API at /command comes in, the method
     * querys the AI controller to get an answer for the command. It then
     * returns that answer to the origin of the request.
     * 
     * @return An answer to an incoming command
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

    /**
     * Returns a list of example commands to the frontend. When a request to the
     * API at /exampleCommands comes in (usually on page load), the method
     * querys the AI controller to get a list of possible commands. It then
     * returns that list to the origin of the request.
     * 
     * @return A list of possible commands
     */
    @CrossOrigin(origins = { "http://localhost:4141",
            "http://srv-lab-t-825:4141" })
    @RequestMapping("/exampleCommands")
    public List<ExampleCall> sendExampleCommands() {
        ArrayList<ExampleCall> exampleCallList = new ArrayList<ExampleCall>();

        ExampleCall test1 = new ExampleCall();
        test1.setCall("test example 1");
        ExampleCall test2 = new ExampleCall();
        test2.setCall("test example2");
        exampleCallList.add(test1);
        exampleCallList.add(test2);

        return exampleCallList;
    }

}
