package nextstep.subway.domain;

import nextstep.common.exception.AuthorityException;

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
    private Long upStationId;
    private Long downStationId;

    protected Favorite() {
    }

    public Favorite(Long memberId, Long upStationId, Long downStationId) {
        this(null, memberId, upStationId, downStationId);
    }

    private Favorite(Long id, Long memberId, Long upStationId, Long downStationId) {
        this.id = id;
        this.memberId = memberId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public boolean isOwner(Long memberId) {
        if (this.memberId.equals(memberId)) {
            return true;
        }
        throw new AuthorityException();
    }
}
