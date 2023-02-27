package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favorite {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    private Long memberId;
    private Long departureStationId;
    private Long destinationStationId;

    protected Favorite() {}

    public Favorite(Long memberId, Long departureStationId, Long destinationStationId) {
        this.memberId = memberId;
        this.departureStationId = departureStationId;
        this.destinationStationId = destinationStationId;
    }

    public static Favorite of(Long memberId, Long departureStationId, Long destinationStationId) {
        return new Favorite(memberId, departureStationId, destinationStationId);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getDepartureStationId() {
        return departureStationId;
    }

    public Long getDestinationStationId() {
        return destinationStationId;
    }
}
