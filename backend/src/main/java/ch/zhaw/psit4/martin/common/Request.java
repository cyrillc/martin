package ch.zhaw.psit4.martin.common;

public class Request {
    
    public Request(String command){
        setCommand(command);
    }
    
    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
