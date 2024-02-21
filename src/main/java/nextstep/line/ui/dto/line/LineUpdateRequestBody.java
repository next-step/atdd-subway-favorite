package nextstep.line.ui.dto.line;

import nextstep.line.application.dto.line.UpdateLineCommand;

import javax.validation.constraints.NotBlank;

public class LineUpdateRequestBody {
    @NotBlank
    private String name;
    @NotBlank
    private String color;

    public LineUpdateRequestBody(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public UpdateLineCommand toCommand(Long lineId) {
        return new UpdateLineCommand(lineId, name, color);
    }
}
