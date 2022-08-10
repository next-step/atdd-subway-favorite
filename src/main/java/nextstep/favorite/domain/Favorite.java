package nextstep.favorite.domain;

import nextstep.favorite.util.BaseTimeEntity;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Station target;

    public Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    protected Favorite() {

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
