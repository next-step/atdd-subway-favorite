package nextstep.subway.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
public class Favorite{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public Favorite() {}

    public Favorite(Station sourceStation, Station targetStation, LocalDateTime modifiedDate) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.modifiedDate = modifiedDate;
    }

    @PrePersist
    public void createdDt(){
        this.createdDate = LocalDateTime.now();
    }
}