package nextstep.subway.domain.entity.favorite;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.domain.command.FavoriteCommand;

import javax.persistence.*;

@Getter
@Entity(name = "favorites")
@AllArgsConstructor
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
}
