package atdd.path.domain;

public class FavoriteStation {

    private Long id;
    private User user;
    private Station station;

    public FavoriteStation() {
    }

    public FavoriteStation(User user, Station station) {
        this.user = user;
        this.station = station;
    }

    public FavoriteStation(Long id, User user, Station station) {
        this.id = id;
        this.user = user;
        this.station = station;
    }

    public static FavoriteStation of(User user, Station station) {
        return new FavoriteStation(user, station);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Station getStation() {
        return station;
    }
}
