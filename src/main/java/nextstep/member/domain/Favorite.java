package nextstep.member.domain;

import nextstep.error.exception.BusinessException;
import nextstep.subway.domain.Station;

import javax.persistence.*;
import java.util.Objects;

import static nextstep.error.exception.ErrorCode.NOT_DELETE_OTHER_FAVORITE;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    @ManyToOne
    @JoinColumn(name = "up_station_id")
    private Station source;
    @ManyToOne
    @JoinColumn(name = "down_station_id")
    private Station target;

    protected Favorite() {
    }

    public Favorite(Long memberId, Station source, Station target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }
    public void validateFavorite(Long memberId) {
        if (!Objects.equals(this.memberId, memberId)) {
            throw new BusinessException(NOT_DELETE_OTHER_FAVORITE);
        }
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
}
