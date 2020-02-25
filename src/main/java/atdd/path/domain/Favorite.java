package atdd.path.domain;

import java.util.HashMap;
import java.util.Map;

import static atdd.path.dao.FavoriteDao.STATION_ID_KEY;
import static atdd.path.dao.UserDao.ID_KEY;

public class Favorite {
    private Long id;
    private Station station;
    private Line line;

    public Favorite() {
    }

    public Favorite(Long id, Station station) {
        this.id = id;
        this.station = station;
    }

    public Favorite(Station station) {
        this.id = 0L;
        this.station = station;
    }

    public static Map<String, Object> getSaveParameterByFavorite(Favorite favorite) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ID_KEY, favorite.getId());
        parameters.put(STATION_ID_KEY, favorite.getStation().getId());
        return parameters;
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
