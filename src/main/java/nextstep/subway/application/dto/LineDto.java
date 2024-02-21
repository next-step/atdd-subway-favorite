package nextstep.subway.application.dto;

import nextstep.subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineDto {

    private Long lineId;
    private String name;
    private String color;
    private List<SectionDto> sections;

    private LineDto() {
    }

    private LineDto(Long lineId, String name, String color, List<SectionDto> sections) {
        this.lineId = lineId;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public static LineDto from(Line line) {
        return new LineDto(
                line.getLineId(),
                line.getName(),
                line.getColor(),
                line.getSections().stream()
                        .map(SectionDto::from)
                        .collect(Collectors.toList())
        );
    }

    public Long getLineId() {
        return lineId;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<SectionDto> getSections() {
        return sections;
    }

}
