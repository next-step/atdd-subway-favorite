package nextstep.subway.domain;

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

    @JoinColumn(name = "source_id")
    @ManyToOne
    private Station source;

    @JoinColumn(name = "target_id")
    @ManyToOne
    private Station target;

    @Column(name = "member_id")
    private Long memberId;

    public Favorite() {
    }

    public Favorite(Station source, Station target, Long memberId) {
        this.source = source;
        this.target = target;
        this.memberId = memberId;
    }

    public static Favorite of(Station source, Station target, Long memberId) {
        return new Favorite(source, target, memberId);
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


    public void validDelete(Long memberId) {
        if(!isEqualsMember(memberId)){
            throw new IllegalArgumentException();
        }
    }

    private boolean isEqualsMember(Long memberId) {
        return this.memberId.equals(memberId);
    }

}
