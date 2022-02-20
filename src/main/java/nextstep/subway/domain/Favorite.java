package nextstep.subway.domain;

import nextstep.member.domain.Member;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Member member;

    @OneToOne
    private Station source;

    @OneToOne
    private Station target;

    protected Favorite() {
    }

    public Favorite(Member member,Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
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
}
