package atdd.path.application;

import atdd.path.application.dto.favorite.FavoriteCreateRequestView;
import atdd.path.dao.FavoriteDao;
import atdd.path.domain.Favorite;
import atdd.path.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    private FavoriteDao favoriteDao;

    public FavoriteService(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    public Favorite save(User loginUser, FavoriteCreateRequestView favorite) {
        return favoriteDao.save(favorite.toEntity(loginUser));
    }

    public List<Favorite> findByUser(User loginUser) {
        return favoriteDao.findByUser(loginUser);
    }
}
