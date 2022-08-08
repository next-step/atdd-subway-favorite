package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.member.domain.Member;

@Entity
public class Favorite {

  @Id
  @Column(name = "favorite_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "source_id")
  private Station sourceStation;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "target_id")
  private Station targetStation;

  @Column(name = "member_id")
  private Long memberId;

  protected Favorite() {}

  public Favorite(Station sourceStation, Station targetStation, Long memberId) {
    this.sourceStation = sourceStation;
    this.targetStation = targetStation;
    this.memberId = memberId;
  }

  public Long getId() {
    return id;
  }

  public Station getSourceStation() {
    return sourceStation;
  }

  public Station getTargetStation() {
    return targetStation;
  }
}
