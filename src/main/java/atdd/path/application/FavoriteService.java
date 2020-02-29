package atdd.path.application;

import atdd.path.application.dto.favorite.FavoriteCreateRequestView;
import atdd.path.application.dto.favorite.FavoriteCreateResponseView;
import atdd.path.application.dto.favorite.FavoriteListResponseView;
import atdd.path.dao.FavoriteDao;
import atdd.path.domain.User;
import org.springframework.stereotype.Service;

import static atdd.path.dao.FavoriteDao.STATION_TYPE;

@Service
public class FavoriteService {
    private FavoriteDao favoriteDao;

    public FavoriteService(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    public FavoriteCreateResponseView save(User loginUser, FavoriteCreateRequestView favorite) {
        return FavoriteCreateResponseView.toDtoEntity(favoriteDao.save(favorite.toEntity(loginUser), favorite.getType()));
    }

    public FavoriteListResponseView findByUser(User loginUser, String type) {
        if (STATION_TYPE.equals(type)) {
            return FavoriteListResponseView.toDtoEntity(favoriteDao.findStationByUser(loginUser));
        }

        return FavoriteListResponseView.toDtoEntity(favoriteDao.findEdgeByUser(loginUser));
    }

    public void deleteFavoriteItem(User loginUser, Long itemId) {
        favoriteDao.deleteStation(loginUser, itemId);
    }
}
