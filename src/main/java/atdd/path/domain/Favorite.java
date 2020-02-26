package atdd.path.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static atdd.path.dao.FavoriteDao.STATION_ID_KEY;
import static atdd.path.dao.FavoriteDao.USER_ID_KEY;
import static atdd.path.dao.UserDao.ID_KEY;

@NoArgsConstructor
public class Favorite {
    private Long id;
    private User user;
    private Station station;
    private Line line;

    @Builder
    public Favorite(Long id, User user, Station station) {
        this.id = id;
        this.user = user;
        this.station = station;
    }

    public Favorite(User user, Station station) {
        this.id = 0L;
        this.user = user;
        this.station = station;
    }

    public static Map<String, Object> getSaveParameterByFavorite(Favorite favorite) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ID_KEY, favorite.getId());
        parameters.put(USER_ID_KEY, favorite.getUserId());
        parameters.put(STATION_ID_KEY, favorite.getStationId());
        return parameters;
    }

    private Long getStationId() {
        return getStation().getId();
    }

    private Long getUserId() {
        return getUser().getId();
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public Station getStation() {
        return station;
    }

    public Line getLine() {
        return line;
    }
}
