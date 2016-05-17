package ch.zhaw.psit4.martin.api.types.output;

public enum MOutputType {

    TEXT("text"), IMAGE("image"), HEADING("heading");

    private String name;

    private MOutputType(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }


}
