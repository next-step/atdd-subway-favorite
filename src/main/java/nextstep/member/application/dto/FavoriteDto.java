package nextstep.member.application.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Station;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteDto {

    private Long id;
    private Station target;
    private Station source;

    public static FavoriteDto of(Long id, Station target, Station source) {
        return new FavoriteDto(id, target, source);
    }
}
