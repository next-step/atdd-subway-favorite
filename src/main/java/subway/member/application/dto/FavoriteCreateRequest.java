package subway.member.application.dto;

import lombok.Getter;

import javax.validation.constraints.Min;

@Getter
public class FavoriteCreateRequest {

    private static final String SOURCE_NUMERIC = "시작 역 번호가 올바르지 않습니다.";
    private static final String TARGET_NUMERIC = "시작 역 번호가 올바르지 않습니다.";

    @Min(value = 1, message = SOURCE_NUMERIC)
    private Long source;
    @Min(value = 1, message = TARGET_NUMERIC)
    private Long target;
}
