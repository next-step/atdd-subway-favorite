package nextstep.favorite.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Station;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", referencedColumnName = "id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", referencedColumnName = "id")
    private Station target;

    public Favorite(Long memberId, Station source, Station target) {
        this.memberId = memberId;
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

    public boolean isOwner(Long memberId) {
        return this.memberId.equals(memberId);
    }
}