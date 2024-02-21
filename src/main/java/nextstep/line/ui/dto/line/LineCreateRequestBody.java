package nextstep.line.ui.dto.line;

import nextstep.line.application.dto.line.AddLineCommand;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class LineCreateRequestBody {
    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @NotBlank
    private Long upStationId;
    @NotBlank
    private Long downStationId;
    @NotBlank
    @Min(value = 1)
    private int distance;

    public LineCreateRequestBody(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public AddLineCommand toCommand() {
        return new AddLineCommand(name, color, upStationId, downStationId, distance);
    }
}
