package atdd.path.application;

import atdd.path.application.dto.favorite.FavoriteCreateRequestView;
import atdd.path.application.dto.favorite.FavoriteCreateResponseView;
import atdd.path.application.dto.favorite.FavoriteListResponseView;
import atdd.path.dao.FavoriteDao;
import atdd.path.domain.Favorite;
import atdd.path.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static atdd.path.dao.FavoriteDao.EDGE_TYPE;
import static atdd.path.dao.FavoriteDao.STATION_TYPE;

@Service
public class FavoriteService {
    private FavoriteDao favoriteDao;

    public FavoriteService(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    @Transactional
    public FavoriteCreateResponseView save(User loginUser, FavoriteCreateRequestView favorite) {
        if (EDGE_TYPE.equals(favorite.getType())) {
            favorite.getEdge().validateFavoriteEdge();
        }

        return FavoriteCreateResponseView.toDtoEntity(favoriteDao.save(favorite.toEntity(loginUser), favorite.getType()));
    }

    @Transactional(readOnly = true)
    public FavoriteListResponseView findByUser(User loginUser, String type) {
        return FavoriteListResponseView.toDtoEntity(findStationByUserAndType(loginUser, type));
    }

    @Transactional
    public void deleteItem(User loginUser, Long itemId) {
        favoriteDao.deleteItem(loginUser, itemId);
    }

    private List<Favorite> findStationByUserAndType(User loginUser, String type) {
        if (STATION_TYPE.equals(type)) {
            return favoriteDao.findStationByUser(loginUser);
        }

        return favoriteDao.findEdgeByUser(loginUser);
    }
}
