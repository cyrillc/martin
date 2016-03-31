package ch.zhaw.psit4.martin.aiController;

import ch.zhaw.psit4.martin.common.Request;

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
    private Request request;
    private Modul modul;

    /**
     * 
     */
    public AIControllerFacade() {
        // TODO Auto-generated constructor stub
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Modul getModul() {
        return modul;
    }

    public void setModul(Modul modul) {
        this.modul = modul;
    }

}
