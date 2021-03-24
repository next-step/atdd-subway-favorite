package nextstep.subway.favorite.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import nextstep.subway.common.BaseEntity;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

@Entity
public class Favorite extends BaseEntity {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "source_id")
  private Station source;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "target_id")
  private Station target;

  public Favorite(){

  }

  public Favorite(Member member, Station source, Station target) {
    this.member = member;
    this.source = source;
    this.target = target;
  }

  public Long getId() {
    return id;
  }

  public Member getMember() {
    return member;
  }

  public Station getSource() {
    return source;
  }

  public Station getTarget() {
    return target;
  }
}
