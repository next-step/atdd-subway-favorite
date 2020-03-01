package atdd.path.application;

import atdd.path.dao.FavoriteDao;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.FavoriteStation;
import atdd.path.domain.Station;
import atdd.path.domain.User;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteDao favoriteDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public FavoriteService(FavoriteDao favoriteDao, StationDao stationDao, LineDao lineDao) {
        this.favoriteDao = favoriteDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public FavoriteStation saveFavoriteStation(Long stationId, User user) {
        Station findStation = stationDao.findById(stationId);
        return favoriteDao.saveFavoriteStationByUser(findStation.getId(), user);
    }
}
