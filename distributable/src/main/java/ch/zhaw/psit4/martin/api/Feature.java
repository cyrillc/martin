package ch.zhaw.psit4.martin.api;

import java.util.Map;

import ch.zhaw.psit4.martin.api.types.IBaseType;;

/**
 * Work object used by {@link IMartinContext} with callbacks.
 * 
 * Martin calls the methods of registered WorkItems if the AIController
 * allocates a timeslot for them. Workitems allow a plugin to do work
 * asynchronously.
 *
 * @version 0.0.1-SNAPSHOT
 */
public class Feature {

    /**
     * The unique ID of this item. The id is distributed by the
     * {@link IPluginContext} implementation.
     */
    private Long id;
    /**
     * The unique requestID of this item. The id is distributed by the
     * {@link IPluginContext} implementation.
     */
    private Long requestID;

    /**
     * Private empty constructor to force plugin implementations to set the ID
     */
    @SuppressWarnings("unused")
    private Feature() {}

    public Feature(long requestID) {
        setRequestID(requestID);
    }

    /**
     * Set the unique id of this WorkItem. The id can only be set once, if it's
     * set a second time that will result in a runtime-exception.
     * 
     * @param id
     *            the new id of this Item.
     */
    public void setID(long id) {
        this.id = (this.id == null) ? id : throwIDSetException();
    }

    public long getID() {
        return this.id;
    }

    /**
     * Set the unique requestID of this WorkItem. It associates the Wortitem
     * with a request. The id can only be set once, if it's set a second time
     * that will result in a runtime-exception.
     * 
     * @param id
     *            the new id of this Item.
     */
    public void setRequestID(long id) {
        this.requestID = (this.requestID == null) ? id : throwIDSetException();
    }

    public long getRequestID() {
        return this.requestID;
    }

    private int throwIDSetException() {
        throw new RuntimeException("ERROR: id is already set.");
    }

    /**
     * Called if the work is started by MArtIn.
     * 
     * @param args
     *            {@link HashMap} filled with initialization arguments.
     * @throws Exception
     *             An excpetion occured during work start.
     */
    public void start(final Map<String, IBaseType> args) throws Exception {}

    /**
     * Main method called by MArtIn.
     * 
     * @throws Exception
     *             An excpetion occured during work.
     */
    public void run() throws Exception {}

    /**
     * Called if the work has ended.
     * 
     * @return The answer in String format.
     * @throws Exception
     *             An excpetion occured during work end.
     */
    public String stop() throws Exception {
        return null;
    }
}
