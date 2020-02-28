package atdd.path.application;

import atdd.path.application.dto.favorite.FavoriteCreateRequestView;
import atdd.path.application.dto.favorite.FavoriteCreateResponseView;
import atdd.path.application.dto.favorite.FavoriteListResponseView;
import atdd.path.dao.FavoriteDao;
import atdd.path.domain.User;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private FavoriteDao favoriteDao;

    public FavoriteService(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    public FavoriteCreateResponseView save(User loginUser, FavoriteCreateRequestView favorite) {
        return FavoriteCreateResponseView.toDtoEntity(favoriteDao.save(favorite.toEntity(loginUser), favorite.getType()));
    }

    public FavoriteListResponseView findByUser(User loginUser) {
        return FavoriteListResponseView.toDtoEntity(favoriteDao.findByUser(loginUser));
    }

    public void deleteStation(User loginUser, Long stationId) {
        favoriteDao.deleteStation(loginUser, stationId);
    }
}
