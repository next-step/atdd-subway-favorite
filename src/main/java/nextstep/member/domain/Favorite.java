package nextstep.member.domain;

import nextstep.subway.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favorite extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long sourceId;

  private Long targetId;

  private Long memberId;

  protected Favorite() {}

  public Favorite(Long sourceId, Long targetId, Long memberId) {
    this.sourceId = sourceId;
    this.targetId = targetId;
    this.memberId = memberId;
  }

  public Long getId() {
    return id;
  }

  public Long getSourceId() {
    return sourceId;
  }

  public Long getTargetId() {
    return targetId;
  }

}
