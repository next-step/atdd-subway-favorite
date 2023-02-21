package nextstep.favorite.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.favorite.application.exception.FavoriteCreateException;
import nextstep.favorite.application.exception.FavoriteErrorCode;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station source;


    @ManyToOne(fetch = FetchType.LAZY)
    private Station target;

    public Favorite(Member member, Station source, Station target) {
        validateStation(source, target);
        validateEqualStartAndDestination(source, target);

        this.member = member;
        this.source = source;
        this.target = target;
    }

    private void validateStation(Station source, Station target) {
        if (source == null || target == null) {
            throw new FavoriteCreateException(FavoriteErrorCode.INVALID_CREATE_REQUEST_STATION);
        }
    }

    private void validateEqualStartAndDestination(Station source, Station target) {
        if (source == target) {
            throw new FavoriteCreateException(FavoriteErrorCode.INVALID_CREATED_EQUAL_STATION);
        }
    }

    public boolean isCreatedBy(Member member) {
        return this.id.equals(member.getId());
    }
}
