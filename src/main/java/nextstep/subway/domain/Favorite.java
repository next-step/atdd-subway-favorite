package nextstep.subway.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sourceId;

    private Long targetId;

    private Long memberId;

    public Favorite(Long sourceId, Long targetId, Long memberId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.memberId = memberId;
    }

    public static Favorite of(Long sourceId, Long targetId, Long memberId) {
        return new Favorite(sourceId, targetId, memberId);
    }

}
