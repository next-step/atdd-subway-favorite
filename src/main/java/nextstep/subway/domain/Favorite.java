package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import nextstep.member.domain.Member;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Member member;

    @Column(name = "source_station_id")
    private Long source;

    @Column(name = "target_station_id")
    private Long target;

    protected Favorite() {
    }

    public Favorite(final Member member, final Long source, final Long target) {
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

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

}
