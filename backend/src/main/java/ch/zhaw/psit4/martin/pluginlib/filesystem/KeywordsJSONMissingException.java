package ch.zhaw.psit4.martin.pluginlib.filesystem;

public class KeywordsJSONMissingException extends RuntimeException {

    /**
     * The serial version
     */
    private static final long serialVersionUID = 6571516559287010154L;

    public KeywordsJSONMissingException() {
        super();
    }

    public KeywordsJSONMissingException(String s) {
        super(s);
    }

    public KeywordsJSONMissingException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public KeywordsJSONMissingException(Throwable throwable) {
        super(throwable);
    }
}
