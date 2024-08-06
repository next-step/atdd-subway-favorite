package nextstep.line.dto;

public class ModifyLineRequest {

    private String name;
    private String color;

    public ModifyLineRequest() {
    }

    public ModifyLineRequest(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static ModifyLineRequest of(final String name, final String color) {
        return new ModifyLineRequest(name, color);
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }
}

