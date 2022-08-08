package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.member.domain.Member;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    private Favorite(Long id, Long memberId, Station sourceStation, Station targetStation) {
        this.id = id;
        this.memberId = memberId;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public static Favorite create(Long memberId, Station sourceStation, Station targetStation) {
        return create(null, memberId, sourceStation, targetStation);
    }

    public static Favorite create(Long id, Long memberId, Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException();
        }
        return new Favorite(id, memberId, sourceStation, targetStation);
    }

    public boolean isNotOwner(Member member) {
        return !member.isSameId(memberId);
    }

    public Long id() {
        return id;
    }

    public Station sourceStation() {
        return sourceStation;
    }

    public Station targetStation() {
        return targetStation;
    }
}
