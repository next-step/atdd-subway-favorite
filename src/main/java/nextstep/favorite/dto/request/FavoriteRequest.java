package nextstep.favorite.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteRequest {

    @NotNull(message = "출발역은 반드시 입력해야 합니다.")
    private Long source;

    @NotNull(message = "도착역은 반드시 입력해야 합니다.")
    private Long target;

}
