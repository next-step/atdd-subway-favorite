package atdd.path.application;

import atdd.path.application.dto.FavoritePathResponseView;
import atdd.path.application.dto.FavoriteStationResponseView;
import atdd.path.dao.*;
import atdd.path.domain.FavoritePath;
import atdd.path.domain.FavoriteStation;
import atdd.path.domain.Station;
import atdd.path.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteStationDao favoriteStationDao;
    private final FavoritePathDao favoritePathDao;
    private final StationDao stationDao;
    private final UserDao userDao;

    public FavoriteService(FavoriteStationDao favoriteStationDao, FavoritePathDao favoritePathDao,
                           StationDao stationDao, UserDao userDao) {
        this.favoriteStationDao = favoriteStationDao;
        this.favoritePathDao = favoritePathDao;
        this.stationDao = stationDao;
        this.userDao = userDao;
    }

    public FavoriteStationResponseView saveFavoriteStation(Long stationId, User user) {
        User findUser = userDao.findByEmail(user.getEmail());
        Station findStation = stationDao.findById(stationId);
        return FavoriteStationResponseView.of(favoriteStationDao.saveFavoriteStation(findStation.getId(), findUser));
    }

    public List<FavoriteStationResponseView> findFavoriteStations(User user) {
        User findUser = userDao.findByEmail(user.getEmail());
        List<FavoriteStation> findFavoriteStations = favoriteStationDao.findFavoriteStationsByUserId(findUser.getId());
        return findFavoriteStations.stream().map(FavoriteStationResponseView::new).collect(Collectors.toList());
    }

    public FavoriteStationResponseView findFavoriteStation(Long id, User user) {
        User findUser = userDao.findByEmail(user.getEmail());
        return FavoriteStationResponseView.of(favoriteStationDao.findFavoriteStationById(id, findUser.getId()));
    }

    public void deleteFavoriteStation(Long id, User user) {
        User findUser = userDao.findByEmail(user.getEmail());
        FavoriteStation favoriteStation = favoriteStationDao.findFavoriteStationByIdAndUserId(id, findUser.getId());
        favoriteStationDao.deleteFavoriteStationById(favoriteStation.getId());
    }


    public FavoritePathResponseView saveFavoritePath(Long startId, Long endId, User user) {
        FavoritePath favoritePath = favoritePathDao.save(user.getId(), startId, endId);
        return FavoritePathResponseView.of(favoritePath);
    }

    public List<FavoritePathResponseView> findFavoritePaths(User user) {
        User findUser = userDao.findByEmail(user.getEmail());
        List<FavoritePath> findFavoritePaths = favoritePathDao.findFavoritePathsByUserId(findUser.getId());
        return FavoritePathResponseView.listOf(findFavoritePaths);
    }

    public void deleteFavoritePath(Long id, User user) {
        User findUser = userDao.findByEmail(user.getEmail());
        FavoritePath favoritePath = favoritePathDao.findFavoritePathByIdAndUserId(id, findUser.getId());
        favoritePathDao.deleteFavoritePathById(favoritePath.getId());

    }
}
