package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import nextstep.member.domain.Member;

@Entity
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Station target;

    @ManyToOne
    private Station source;

    protected Favorite() {
    }

    public Favorite(Member member, Station target, Station source) {
        this.member = member;
        this.target = target;
        this.source = source;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Station getTarget() {
        return target;
    }

    public Station getSource() {
        return source;
    }
}
