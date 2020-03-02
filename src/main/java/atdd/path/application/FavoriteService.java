package atdd.path.application;

import atdd.path.application.dto.FavoriteStationResponseView;
import atdd.path.dao.FavoriteDao;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.dao.UserDao;
import atdd.path.domain.FavoriteStation;
import atdd.path.domain.Station;
import atdd.path.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteDao favoriteDao;
    private final StationDao stationDao;
    private final UserDao userDao;
    private final LineDao lineDao;

    public FavoriteService(FavoriteDao favoriteDao, StationDao stationDao, UserDao userDao, LineDao lineDao) {
        this.favoriteDao = favoriteDao;
        this.stationDao = stationDao;
        this.userDao = userDao;
        this.lineDao = lineDao;
    }

    public FavoriteStationResponseView saveFavoriteStation(Long stationId, User user) {
        User findUser = userDao.findByEmail(user.getEmail());
        Station findStation = stationDao.findById(stationId);
        return FavoriteStationResponseView.of(favoriteDao.saveFavoriteStation(findStation.getId(), findUser));
    }

    public List<FavoriteStationResponseView> findFavoriteStations(User user) {
        User findUser = userDao.findByEmail(user.getEmail());
        List<FavoriteStation> findFavoriteStations = favoriteDao.findFavoriteStationsByUserId(findUser.getId());
        return findFavoriteStations.stream().map(FavoriteStationResponseView::new).collect(Collectors.toList());
    }

    public FavoriteStationResponseView findFavoriteStation(Long id, User user) {
        return FavoriteStationResponseView.of(favoriteDao.findFavoriteStationById(id));
    }

    public void deleteFavoriteStation(Long id, User user) {
        FavoriteStation favoriteStation = Optional.ofNullable(favoriteDao.findFavoriteStationById(id))
                .orElseThrow(IllegalArgumentException::new);
        favoriteDao.deleteFavoriteStationById(favoriteStation.getId());
    }
}
