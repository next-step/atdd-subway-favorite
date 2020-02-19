package atdd.path.application;

import atdd.path.dao.FavoriteDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.FavoritePath;
import atdd.path.domain.FavoriteStation;
import atdd.path.domain.Member;
import atdd.path.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final FavoriteDao favoriteDao;
    private final StationDao stationDao;

    public FavoriteService(FavoriteDao favoriteDao, StationDao stationDao) {
        this.favoriteDao = favoriteDao;
        this.stationDao = stationDao;
    }

    public FavoriteStation saveForStation(Member member, Long stationId) {
        final Station findStation = stationDao.findById(stationId);
        return favoriteDao.saveForStation(new FavoriteStation(member, findStation));
    }

    public FavoritePath saveForPath(Member member, Long startId, Long endId) {
        final Station sourceStation = stationDao.findById(startId);
        final Station targetStation = stationDao.findById(endId);
        return favoriteDao.saveForPath(new FavoritePath(member, sourceStation, targetStation));
    }

}
