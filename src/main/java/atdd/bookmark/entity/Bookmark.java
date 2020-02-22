package atdd.bookmark.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import atdd.path.domain.Station;
import lombok.Builder;

@Entity
@Builder
public class Bookmark {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id")
  private Long userID;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "source_station_id")
  private Station sourceStation;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "target_station_id", nullable = true)
  private Station targetStation;

  public Long getId() {
    return id;
  }

  public Long getUserID() {
    return userID;
  }

  public Long getSourceStationID() {
    return sourceStation.getId();
  }

  public Long getTargetStationID() {
    return targetStation.getId();
  }

  public Station getSourceStation() {
    return sourceStation;
  }

  public Station getTargetStation() {
    return targetStation;
  }
}
