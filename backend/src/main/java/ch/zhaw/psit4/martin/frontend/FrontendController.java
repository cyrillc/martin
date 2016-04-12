package ch.zhaw.psit4.martin.frontend;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.psit4.martin.aiController.AIControllerFacade;
import ch.zhaw.psit4.martin.boot.MartinBoot;
import ch.zhaw.psit4.martin.common.HistoryItem;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;

import ch.zhaw.psit4.martin.pluginlib.db.*;

/**
 * This class connect the Frontend with the AI using REST.
 *
 * @version 0.0.1-SNAPSHOT
 *
 */
@RestController
public class FrontendController {

    /**
     * Returns the answer to a command to the Frontend. When a request to the
     * API at /command comes in, the method
     * querys the AI controller to get an answer for the command. It then
     * returns that answer to the origin of the request.
     *
     * @param command
     * @return the response of the AI
     */
    @CrossOrigin(origins = { "http://localhost:4141",
            "http://srv-lab-t-825:4141", "http://srv-lab-t-825.zhaw.ch:4141" })
    @RequestMapping("/command")
    public Response launchCommand(
            @RequestParam(value = "command") String command) {
        Request request = new Request(command);
        AIControllerFacade aiController = (AIControllerFacade) MartinBoot.context.getBean("AIControllerFacade");
        Response response = aiController.elaborateRequest(request);
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
            "http://srv-lab-t-825:4141","http://srv-lab-t-825.zhaw.ch:4141" })
    @RequestMapping("/exampleCommands")
    public List<ExampleCall> sendExampleCommands() {

        AIControllerFacade aiController = (AIControllerFacade) MartinBoot.context.getBean("AIControllerFacade");
        return aiController.getExampleCalls();

    }
    
    /**
     * 
     * @return A list of HistoryItems, with all user Requests and relative Responses.
     */
    @CrossOrigin(origins = { "http://localhost:4141",
            "http://srv-lab-t-825:4141","http://srv-lab-t-825.zhaw.ch:4141" })
    @RequestMapping("/history")
    public List<HistoryItem> getHistory() {
        AIControllerFacade aiController = (AIControllerFacade) MartinBoot.context.getBean("AIControllerFacade");
        return aiController.getHistory();
    }
}
