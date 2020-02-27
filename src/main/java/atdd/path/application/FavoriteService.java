package atdd.path.application;

import atdd.path.application.dto.favorite.FavoriteCreateRequestView;
import atdd.path.application.dto.favorite.FavoriteListResponseView;
import atdd.path.dao.FavoriteDao;
import atdd.path.domain.Favorite;
import atdd.path.domain.User;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private FavoriteDao favoriteDao;

    public FavoriteService(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    public Favorite save(User loginUser, FavoriteCreateRequestView favorite) {
        return favoriteDao.save(favorite.toEntity(loginUser));
    }

    public FavoriteListResponseView findByUser(User loginUser) {
        return FavoriteListResponseView.toDtoEntity(favoriteDao.findByUser(loginUser));
    }

    public void deleteStation(User loginUser, Long stationId) {
        favoriteDao.deleteStation(loginUser, stationId);
    }
}
