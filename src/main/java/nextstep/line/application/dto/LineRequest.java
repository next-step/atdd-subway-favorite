package nextstep.line.application.dto;

import nextstep.common.EntitySupplier;
import nextstep.line.domain.Line;

import javax.validation.constraints.NotBlank;

public class LineRequest implements EntitySupplier<Line> {
    @NotBlank
    private String name;
    @NotBlank
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    @Override
    public Line toEntity() {
        return new Line(name, color);
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
}
