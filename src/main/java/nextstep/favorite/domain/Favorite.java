package nextstep.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.member.domain.Member;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private Long sourceStationId;
    private Long targetStationId;

    public Favorite() {}

    public Favorite(Long memberId, Long sourceStationId, Long targetStationId) {
        this(null, memberId, sourceStationId, targetStationId);
    }

    public Favorite(Builder builder) {
        this(null, builder.memberId, builder.sourceStationId, builder.targetStationId);
    }

    public Favorite(Long id, Long memberId, Long sourceStationId, Long targetStationId) {
        this.id = id;
        this.memberId = memberId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public boolean isMember(Member member) {
        return memberId.equals(member.getId());
    }

    public static class Builder {
        private Long id;
        private Long memberId;
        private Long sourceStationId;
        private Long targetStationId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder memberId(Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder sourceStationId(Long sourceStationId) {
            this.sourceStationId = sourceStationId;
            return this;
        }

        public Builder targetStationId(Long targetStationId) {
            this.targetStationId = targetStationId;
            return this;
        }

        public Favorite build() {
            return new Favorite(this);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
