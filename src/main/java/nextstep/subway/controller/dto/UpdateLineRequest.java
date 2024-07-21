package nextstep.subway.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import nextstep.subway.domain.command.LineCommand;

@ToString
@Getter
@AllArgsConstructor
public class UpdateLineRequest {
    private String name;
    private String color;

    public LineCommand.UpdateLine toCommand(Long id) {
        return new LineCommand.UpdateLine(id, name, color);
    }
}
