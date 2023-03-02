package nextstep.member.domain;

import nextstep.common.exception.AuthorizationException;
import nextstep.subway.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long memberId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_station_id")
    private Station source;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_station_id")
    private Station target;

    public Favorite() {
    }

    public Favorite(Long memberId, Station source, Station target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public void validateMember(Long memberId) {
        if (!Objects.equals(this.memberId, memberId)) {
            throw new AuthorizationException();
        }
    }
}
