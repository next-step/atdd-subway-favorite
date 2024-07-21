package nextstep.subway.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import nextstep.subway.domain.command.LineCommand;

@ToString
@Getter
@AllArgsConstructor
public class AddSectionRequest {
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public LineCommand.AddSection toCommand(Long id) {
        return new LineCommand.AddSection(id, upStationId, downStationId, distance);
    }
}
