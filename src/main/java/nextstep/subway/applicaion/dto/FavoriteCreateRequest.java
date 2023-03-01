package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

import javax.validation.constraints.NotNull;
import java.util.function.Function;

@Getter
public class FavoriteCreateRequest {

    @NotNull(message = "source is null")
    private Long source;

    @NotNull(message = "target is null")
    private Long target;
    private Long memberId;

    public Favorite toEntity(Function<Long, Station> findFunction) {
        return Favorite.of(
                memberId,
                findFunction.apply(source),
                findFunction.apply(target));
    }

    public void addMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
