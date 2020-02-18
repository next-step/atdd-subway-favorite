package atdd.path.domain;

public class FavoriteStation {

    private Long id;
    private Station station;

    public FavoriteStation(Long id, Station station) {
        this.id = id;
        this.station = station;
    }

    public Long getId() {
        return id;
    }

    public Station getStation() {
        return station;
    }

}
