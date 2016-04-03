package ch.zhaw.psit4.martin.aiController;

import ch.zhaw.psit4.martin.common.ExtendedRequest;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;
import ch.zhaw.psit4.martin.pluginlib.IPluginLibrary;
import ch.zhaw.psit4.martin.requestProcessor.RequestProcessor;

/**
 * This class represents the AIControllerFacade The class follows the Facade
 * Pattern and should wrap the whole subsystem of AIController. This offers an
 * unique Interface to the outside.
 * 
 * @version 1.0
 * @author marco
 *
 */
public class AIControllerFacade {
	
	private RequestProcessor requestProcessor;
	private IPluginLibrary library;
	
	public AIControllerFacade(){
		this.requestProcessor = new RequestProcessor();
	}
	
	public void setLibrary(IPluginLibrary library){
		this.library = library;
		this.requestProcessor.setLibrary(library);
	}
	
    
    public Response elaborateRequest(Request request){
    	try {
    		ExtendedRequest extendedRequest = this.requestProcessor.extend(request);
    		return library.executeRequest(extendedRequest);
    	} catch(Exception e){
    		return new Response("Sorry, I cant't understand you.");
    	}
    }    
}
