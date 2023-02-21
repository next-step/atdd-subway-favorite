package nextstep.subway.domain;

import nextstep.member.domain.Member;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_id")
    private Station target;

    public Favorite(Member member, Station source, Station target) {
        this.memberId = member.getId();
        this.source = source;
        this.target = target;
    }

    protected Favorite() {

    }

    public Station getTarget() {
        return target;
    }

    public Station getSource() {
        return source;
    }

    public Long getId() {
        return id;
    }

}
