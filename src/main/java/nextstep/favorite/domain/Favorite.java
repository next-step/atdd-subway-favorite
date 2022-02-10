package nextstep.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import nextstep.common.domain.model.BaseEntity;
import nextstep.member.domain.Member;
import nextstep.station.domain.Station;

@Getter
@Entity
public class Favorite extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @JoinColumn(name = "FAVORITE_MEMBER", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "FAVORITE_SOURCE", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Station source;

    @JoinColumn(name = "FAVORITE_TARGET", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Station target;

    protected Favorite() {
    }

    @Builder
    public Favorite(Long id, Member member, Station source, Station target) {
        Id = id;
        this.member = member;
        this.source = source;
        this.target = target;
    }
}
