package nextstep.subway.applicaion.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FavoriteRequest {

    @NotNull(message = "출발역은 반드시 입력해야합니다.")
    private Long source;

    @NotNull(message = "도착역은 반드시 입력해야합니다.")
    private Long target;

}
