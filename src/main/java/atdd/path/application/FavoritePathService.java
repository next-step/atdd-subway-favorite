package atdd.path.application;


import atdd.path.dao.FavoritePathDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.FavoritePath;
import atdd.path.domain.Station;
import atdd.user.dao.UserDao;
import atdd.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoritePathService {
    @Autowired
    private FavoritePathDao favoritePathDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private StationDao stationDao;

    public FavoritePath addFavoritePath(final String email, FavoritePath favoritePath) {
        User user = userDao.findByEmail(email);

        favoritePath.setOwner(user.getId());

        Station sourceStation = stationDao.findById(favoritePath.getSourceStationId());
        Station tartStation = stationDao.findById(favoritePath.getTargetStationId());

        long favoritePathId = favoritePathDao.save(favoritePath).getId();

        favoritePath.setId(favoritePathId);
        favoritePath.setSourceStation(sourceStation);
        favoritePath.setTargetStation(tartStation);

        return favoritePath;
    }
}
