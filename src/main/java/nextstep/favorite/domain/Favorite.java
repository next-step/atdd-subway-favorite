package nextstep.favorite.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private Long sourceId;
    private Long targetId;

    public Favorite() {
    }

    public Favorite(Long memberId, Long sourceId, Long targetId) {
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public boolean isCreatedMember(Long memberId) {
        if (Objects.equals(this.memberId, memberId)) {
            return true;
        }
        return false;
    }
}
