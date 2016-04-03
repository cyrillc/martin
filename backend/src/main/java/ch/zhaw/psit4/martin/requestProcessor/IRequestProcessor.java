package ch.zhaw.psit4.martin.requestProcessor;

import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;

/**
 * This Interface represents the Interface to Request Processor.
 * 
 * @version 1.0
 *
 */

public interface IRequestProcessor {
    /**
     * Extends a request from a basic command and tries to determine possible module calls.
     * 
     * @param request 
     * @return Returns an ExtendedRequest with original-request and a possible executable function calls.
     */
	public ExtendedRequest extend(Request request) throws Exception;
   
    /**
     * Sets the plugin-library.
     * @param library
     */
	public void setLibrary(IPluginLibrary library);

}
