package atdd.path.application;

import atdd.path.dao.FavoriteStationDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.FavoriteStation;
import atdd.path.domain.Station;
import atdd.user.dao.UserDao;
import atdd.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public class FavoriteStationService {
    private FavoriteStationDao favoriteStationDao;
    private StationDao stationDao;
    private UserDao userDao;

    public FavoriteStationService(FavoriteStationDao favoriteStationDao, StationDao stationDao, UserDao userDao) {
        this.favoriteStationDao = favoriteStationDao;
        this.stationDao = stationDao;
        this.userDao = userDao;
    }

    public FavoriteStation addFavoriteStation(final String email, final long stationId) {
        User user = userDao.findByEmail(email);
        Station station = stationDao.findById(stationId);

        return favoriteStationDao.save(FavoriteStation.builder()
                .owner(user.getId())
                .stationId(station.getId())
                .build());
    }
}
