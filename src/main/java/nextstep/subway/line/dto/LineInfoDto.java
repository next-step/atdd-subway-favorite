package nextstep.subway.line.dto;

import lombok.Getter;

@Getter
public class LineInfoDto {

    private final String name;
    private final String color;
    private final Long distance;

    public LineInfoDto(String name, String color, Long distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
    }
}
