package nextstep.favorite.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sourceStationId")
    private Station sourceStation;

    @ManyToOne
    @JoinColumn(name = "targetStationId")
    private Station targetStation;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @Builder
    public Favorite(Station sourceStation, Station targetStation, Member member) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.member = member;
    }
}
