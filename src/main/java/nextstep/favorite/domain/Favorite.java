package nextstep.favorite.domain;

import nextstep.member.domain.Member;
import nextstep.station.domain.Station;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Entity
@Table(name = "favorite")
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

    public Favorite(Station source, Station target, Member member) {
        this.source = source;
        this.target = target;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public boolean isOwner(Member member) {
        return this.member.equals(member);
    }
}
