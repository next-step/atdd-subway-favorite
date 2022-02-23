package nextstep.member.domain;

import nextstep.subway.domain.BaseEntity;
import nextstep.subway.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long memberId;

  @ManyToOne
  private Station source;

  @ManyToOne
  private Station target;

  protected Favorite() {}

  public Favorite(Long memberId, Station source, Station target) {
    this.memberId = memberId;
    this.source = source;
    this.target = target;
  }

  public Long getId() {
    return id;
  }

  public Long getMemberId() {
    return memberId;
  }

  public Station getSource() {
    return source;
  }

  public Station getTarget() {
    return target;
  }
}
