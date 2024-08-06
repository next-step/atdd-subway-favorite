package nextstep.line.payload;

public class UpdateLineRequest {
    private String name;
    private String color;

    public UpdateLineRequest() {
    }

    public UpdateLineRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
