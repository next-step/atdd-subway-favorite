package nextstep.favorite.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.entity.Member;
import nextstep.subway.station.entity.Station;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station source;
    @ManyToOne
    @JoinColumn(name = "target_id")
    private Station target;

    @Builder
    public Favorite(Long memberId, Station source, Station target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

}
