package ch.zhaw.psit4.martin.api;

import java.util.Map;

import ch.zhaw.psit4.martin.api.types.IMartinType;

/**
 * Work object used by {@link IMartinContext} with callbacks.
 * 
 * Martin calls the methods of registered WorkItems if the AIController
 * allocates a timeslot for them. Workitems allow a plugin to do work
 * asynchronously.
 *
 * @author Daniel Fabian
 * @version 0.0.1-SNAPSHOT
 */
public abstract class WorkItem {

    /**
     * The unique ID of this item. The id is distributed by the
     * {@link IPluginContext} implementation.
     */
    private Long id;

    /**
     * Set the unique id of this WorkItem. The id can only be set once, if it's
     * set a secont time that will result in a runtime-exception.
     * 
     * @param id the new id of this Item.
     */
    public void setID(long id) {
        this.id = (this.id == null) ? id : throwIDSetException();
    }


    public long getID() {
        return this.id;
    }
    
    private int throwIDSetException() {
        throw new RuntimeException("ERROR: id is already set.");
    }

    /**
     * Called if the work is started by MArtIn.
     * 
     * @param args {@link HashMap} filled with initialization arguments.
     * @throws Exception
     *             An excpetion occured during work start.
     */
    public abstract void onWorkStart(final Map<String, IMartinType> args) throws Exception;

    /**
     * Main method called by MArtIn.
     * 
     * @throws Exception
     *             An excpetion occured during work.
     */
    public abstract void doWork() throws Exception;

    /**
     * Called if the work has ended.
     * 
     * @throws Exception
     *             An excpetion occured during work end.
     */
    public abstract void onWorkDone() throws Exception;
}
