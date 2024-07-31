package nextstep.subway.domain.entity.favorite;

import lombok.Getter;
import nextstep.subway.domain.command.FavoriteCommand;
import nextstep.subway.domain.exception.SubwayDomainException;
import nextstep.subway.domain.exception.SubwayDomainExceptionType;

import javax.persistence.*;

@Getter
@Entity(name = "favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long sourceStationId;

    @Column(nullable = false)
    private Long targetStationId;

    protected Favorite() {
    }

    public Favorite(Long memberId, Long sourceStationId, Long targetStationId) {
        this.memberId = memberId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public static Favorite create(FavoriteCommand.CreateFavorite command) {
        return new Favorite(command.getMemberId(), command.getSource(), command.getTarget());
    }

    public void verifyOwner(Long memberId) {
        if (!this.memberId.equals(memberId)) {
            throw new SubwayDomainException(SubwayDomainExceptionType.UNAUTHORIZED_FAVORITE);
        }
    }
}
