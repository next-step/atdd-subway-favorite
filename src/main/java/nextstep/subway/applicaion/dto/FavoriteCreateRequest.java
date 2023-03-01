package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.Function;

@Getter
public class FavoriteCreateRequest {

    @NotNull(message = "source is null")
    private Long source;

    @NotNull(message = "target is null")
    private Long target;
    private Member member;

    public Favorite toEntity(Function<Long, Optional<Station>> findFunction) {
        return Favorite.of(
                member,
                findStation(source, findFunction),
                findStation(target, findFunction));
    }

    private Station findStation(Long id, Function<Long, Optional<Station>> findFunction) {
        return findFunction.apply(id).orElseThrow(() -> new IllegalArgumentException("역을 조회 할 수 없습니다. " + id));
    }

    public void addMember(Member member) {
        this.member = member;
    }
}
