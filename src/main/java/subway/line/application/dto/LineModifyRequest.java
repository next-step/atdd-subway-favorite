package subway.line.application.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class LineModifyRequest {

    private static final String NAME_NOT_BLANK_MODIFY_MESSAGE = "구간 수정을 위해 이름은 필수 값 입니다.";

    @NotBlank(message = NAME_NOT_BLANK_MODIFY_MESSAGE)
    private String name;

    private String color;
}
