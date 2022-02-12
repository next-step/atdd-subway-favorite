package nextstep.favorite.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import nextstep.common.domain.model.BaseEntity;

@Getter
@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "FAVORITE_MEMBER", nullable = false)
    private Long memberId;

    @Column(name = "FAVORITE_SOURCE", nullable = false)
    private Long sourceId;

    @Column(name = "FAVORITE_TARGET", nullable = false)
    private Long targetId;

    protected Favorite() {
    }

    @Builder
    public Favorite(Long id, Long memberId, Long sourceId, Long targetId) {
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }
}
