package nextstep.favorites.domain;

import lombok.Getter;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;

import javax.persistence.*;

@Entity
@Getter
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Station source;

    @ManyToOne
    private Station target;

    @ManyToOne
    private Member member;

    public Favorite() {
    }

    public Favorite(Long id, Station source, Station target, Member member) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public Favorite(Station source, Station target, Member member) {
        this.source = source;
        this.target = target;
        this.member = member;
    }
}
