package nextstep.line.payload;

public class LineStationResponse {
    private Long id;
    private String name;

    public LineStationResponse() {
    }

    public LineStationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
