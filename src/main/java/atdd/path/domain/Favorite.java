package atdd.path.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static atdd.path.dao.FavoriteDao.*;
import static atdd.path.dao.UserDao.ID_KEY;

@NoArgsConstructor
public class Favorite {
    private Long id;
    private User user;
    private Item item;

    @Builder
    public Favorite(Long id, User user, Item item) {
        this.id = id;
        this.user = user;
        this.item = item;
    }

    public Favorite(User user, Item item) {
        this.id = 0L;
        this.user = user;
        this.item = item;
    }

    public static Map<String, Object> getSaveParameterByFavorite(Favorite favorite, String type) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ID_KEY, favorite.getId());
        parameters.put(USER_ID_KEY, favorite.getUserId());
        parameters.put(ITEM_ID_KEY, favorite.getStationId());
        parameters.put(TYPE_KEY, type);
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
        return (Station) item;
    }

    public Line getLine() {
        return (Line) item;
    }
}
