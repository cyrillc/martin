package ch.zhaw.psit4.martin.frontend;

import org.springframework.web.bind.annotation.RestController;

/**
 * Interface for class <code>FrontendController</code>.
 *
 * @author Daniel Fabian
 * @version 0.0.1-SNAPSHOT
 */

@RestController
public interface IFrontendController {
    public void start();
    public Response launchCommand(String command);
}
