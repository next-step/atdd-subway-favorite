package nextstep.subway.favorite.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import nextstep.subway.common.BaseEntity;

@Entity
public class Favorite extends BaseEntity {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "member_id")
  private long memberId;

  @Column(name = "source_id")
  private long sourceId;

  @Column(name = "target_id")
  private long targetId;

  public Favorite(){

  }

  public Favorite(long memberId, long sourceId, long targetId){
    this.memberId = memberId;
    this.sourceId = sourceId;
    this.targetId = targetId;
  }

  public Long getId() {
    return id;
  }

  public long getMemberId() {
    return memberId;
  }

  public long getSourceId() {
    return sourceId;
  }

  public long getTargetId() {
    return targetId;
  }
}
