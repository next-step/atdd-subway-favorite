package nextstep.line.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateLineRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @NotNull
    private Long downStationId;
    @NotNull
    private Long upStationId;
    @NotNull
    private Long distance;

    public CreateLineRequest() {
    }

    public CreateLineRequest(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

    public static CreateLineRequest of(final String name, final String color, final Long upStationId, final Long downStationId, final Long distance) {
        return new CreateLineRequest(name, color, upStationId, downStationId, distance);
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public Long getDistance() {
        return this.distance;
    }

    public Long getUpStationId() {
        return this.upStationId;
    }

    public Long getDownStationId() {
        return this.downStationId;
    }

}

