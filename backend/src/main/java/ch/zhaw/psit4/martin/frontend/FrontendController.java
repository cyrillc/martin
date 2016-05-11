package ch.zhaw.psit4.martin.frontend;

import org.apache.tomcat.util.http.fileupload.FileUploadException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.psit4.martin.aiController.AIControllerFacade;
import ch.zhaw.psit4.martin.common.PluginInformation;
import ch.zhaw.psit4.martin.models.*;
import ch.zhaw.psit4.martin.pluginlib.filesystem.PluginInstaller;

import java.util.List;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * This class connect the Frontend with the AI using REST.
 *
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@MultipartConfig(fileSizeThreshold = 52428800) // upload Max 50MB
public class FrontendController {

    @Autowired
    private AIControllerFacade aiController;
    
    @Autowired
    private PluginInstaller pluginInstaller;

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

    public MResponse launchCommand(
            @RequestParam(value = "command") String command,
            @RequestParam(value = "timed", required = false) boolean timed) {
        MRequest request = new MRequest(command, timed);
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
    public List<MExampleCall> sendExampleCommands() {
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

    public List<MHistoryItem> getHistory(@RequestParam(value = "amount") int amount) {
        return aiController.getLimitedHistory(amount);
    }

    /**
     * Returns the information all MArtIn plugins to the Frontend. When a
     * request to the API at /pluginList comes in, the method querys the AI
     * controller to get an answer for the command. It then returns that answer
     * to the origin of the request.
     *
     * @return the information all MArtIn plugins
     */
    @CrossOrigin(origins = { "http://localhost:4141",
            "http://srv-lab-t-825:4141", "http://srv-lab-t-825.zhaw.ch:4141" })
    @RequestMapping("/pluginList")
    public List<PluginInformation> getPluginList() {
        return aiController.getPluginInformation();
    }

    /**
     * 
     * @return saves the uploaded file from the frontend
     * @throws FileUploadException
     */
    @CrossOrigin(origins = { "http://localhost:4141",
            "http://srv-lab-t-825:4141", "http://srv-lab-t-825.zhaw.ch:4141" })
    @RequestMapping(method = RequestMethod.POST, value = "/plugin/install")
    public String installPlugin(@RequestParam("name") String name,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws FileUploadException {
        
        return pluginInstaller.installPlugin(name, file);
    }

}
