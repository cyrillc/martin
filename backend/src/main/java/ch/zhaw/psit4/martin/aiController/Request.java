package ch.zhaw.psit4.martin.aiController;

/**
 * This class represents the Request itself. All important Information and
 * elaborating Function about a Reguests are stored here.
 */

import java.sql.Date;

public class Request {

    private String request;
    private Date date;

    public Request() {
        // TODO Auto-generated constructor stub
    }

    public void elaborateRequest(String request) {
        this.request = request;
    }

    public void getRequest() {
        // TODO Auto-generated method stub
    }

    public void setRequest() {
        // TODO Auto-generated method stub
    }

}
