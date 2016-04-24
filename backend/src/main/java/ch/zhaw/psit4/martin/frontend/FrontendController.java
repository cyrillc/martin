package ch.zhaw.psit4.martin.frontend;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.psit4.martin.aiController.AIControllerFacade;
import ch.zhaw.psit4.martin.common.PluginInformation;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;
import ch.zhaw.psit4.martin.db.*;
import ch.zhaw.psit4.martin.db.historyitem.HistoryItem;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * This class connect the Frontend with the AI using REST.
 *
 * @version 0.0.1-SNAPSHOT
 *
 */
@RestController
public class FrontendController {

    @Autowired
    private AIControllerFacade aiController;

    /**
     * Returns the answer to a command to the Frontend. When a request to the
     * API at /command comes in, the method querys the AI controller to get an
     * answer for the command. It then returns that answer to the origin of the
     * request.
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
        return aiController.elaborateRequest(request);
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
            "http://srv-lab-t-825:4141", "http://srv-lab-t-825.zhaw.ch:4141" })
    @RequestMapping("/exampleCommands")
    public List<ExampleCall> sendExampleCommands() {
        return aiController.getRandomExampleCalls();
    }

    /**
     * 
     * @return A list of HistoryItems, with all user Requests and relative
     *         Responses.
     */
    @CrossOrigin(origins = { "http://localhost:4141",
            "http://srv-lab-t-825:4141", "http://srv-lab-t-825.zhaw.ch:4141" })
    @RequestMapping("/history")
    public List<HistoryItem> getHistory(
            @RequestParam(value = "amount") int amount) {
        return aiController.getLimitedHistory(amount);
    }
    
    /**
     * Returns the information all MArtIn plugins to the Frontend. When a request to the
     * API at /pluginList comes in, the method querys the AI controller to get an
     * answer for the command. It then returns that answer to the origin of the
     * request.
     *
     * @return the information all MArtIn plugins
     */
    @CrossOrigin(origins = { "http://localhost:4141",
            "http://srv-lab-t-825:4141", "http://srv-lab-t-825.zhaw.ch:4141" })
    @RequestMapping("/pluginList")
    public List<PluginInformation> getPluginList() {
        return aiController.getPluginInformation();
    }
}
