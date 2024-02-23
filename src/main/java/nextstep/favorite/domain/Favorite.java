package nextstep.favorite.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.subway.domain.entity.Station;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station sourceStation;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Station targetStation;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Favorite(Station source, Station target, Member member) {
        this.sourceStation = source;
        this.targetStation = target;
        this.member = member;
    }

}
