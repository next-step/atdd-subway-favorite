package nextstep.favorite.domain;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class Favorite {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long sourceStationId;

  private Long targetStationId;

  private Long memberId;

  @Builder
  public Favorite(Long id, Long sourceStationId, Long targetStationId, Long memberId) {
    this.id = id;
    this.sourceStationId = sourceStationId;
    this.targetStationId = targetStationId;
    this.memberId = memberId;
  }

  public Favorite(Long sourceStationId, Long targetStationId, Long memberId) {
    this(null, sourceStationId, targetStationId, memberId);
  }
}
