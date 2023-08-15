package nextstep.subway.application.dto.station;

public class StationResponse {

    private Long id;
    private String name;

    public StationResponse() {}
    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
}