package nextstep.subway.domain;

import nextstep.member.domain.Member;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Station source;

    @ManyToOne
    private Station target;

    protected Favorite() {
    }

    public Favorite(final Member member, final Station source, final Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
