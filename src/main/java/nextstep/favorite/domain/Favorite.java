package nextstep.favorite.domain;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

  private Favorite(Long sourceStationId, Long targetStationId, Long memberId) {
    this(null, sourceStationId, targetStationId, memberId);
  }

  public static Favorite of(Long sourceStationId, Long targetStationId, Long memberId) {
    return new Favorite(sourceStationId, targetStationId, memberId);
  }

  public boolean isOwner(Long memberId) {
    if (this.memberId == null) {
      return false;
    }
    return this.memberId.equals(memberId);
  }
}
