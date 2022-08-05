package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private Long source;
    private Long target;

    protected Favorite() {
    }

    public Favorite(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public void toMember(Long memberId) {
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
