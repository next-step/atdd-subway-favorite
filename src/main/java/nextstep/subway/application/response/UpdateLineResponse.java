package nextstep.subway.application.response;

import nextstep.subway.application.dto.UpdateLineSectionDto;
import nextstep.subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class UpdateLineResponse {

    private Long lineId;
    private String name;
    private String color;
    private List<UpdateLineSectionDto> sections;

    private UpdateLineResponse() {
    }

    public UpdateLineResponse(Long lineId, String name, String color, List<UpdateLineSectionDto> sections) {
        this.lineId = lineId;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public static UpdateLineResponse from(Line line) {
        return new UpdateLineResponse(
                line.getLineId(),
                line.getName(),
                line.getColor(),
                line.getSections().stream()
                        .map(UpdateLineSectionDto::from)
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

    public List<UpdateLineSectionDto> getSections() {
        return sections;
    }

}
