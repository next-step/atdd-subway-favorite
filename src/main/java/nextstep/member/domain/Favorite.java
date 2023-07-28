package nextstep.member.domain;

import nextstep.subway.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_id")
    private Station target;

    @Column
    private Long memberId;

    public Favorite() {
    }

    public Favorite(Station source, Station target, Long memberId) {
        this.source = source;
        this.target = target;
        this.memberId = memberId;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getId() {
        return id;
    }
}
