package ch.zhaw.psit4.martin.api;

import java.util.List;

import ch.zhaw.psit4.martin.api.types.output.MOutput;

/**
 * The MArtIn Context provided to MArtIn Plugins.
 * 
 * This class handles communication of a plugin with the main application by providing the
 * application with an object, that MArtIn is aware of.
 *
 * @version 0.0.1-SNAPSHOT
 */
public interface IMartinContext {
    
    /**
     * Registers a work item in the context.
     * 
     * @param item The item to register.
     */
    public void registerWorkItem(Feature item);

    /**
     * Registers a response message in MArtIn.
     * 
     * @param response The response to register.
     */
    public void registerResponseMessage(String response);
    
    
    /**
     * Adds an output to the queue to be send to clients.
     * 
     * @param output The list of outputs to send
     */
    public void addToOutputQueue(List<MOutput> output);
}
