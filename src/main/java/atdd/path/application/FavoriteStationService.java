package atdd.path.application;

import atdd.path.application.dto.FavoriteStationResponse;
import atdd.path.dao.FavoriteStationDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.FavoriteStation;
import atdd.path.domain.Station;
import atdd.user.dao.UserDao;
import atdd.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public FavoriteStationResponse addFavoriteStation(final String email, final long stationId) {
        User user = userDao.findByEmail(email);
        Station station = stationDao.findById(stationId);

        FavoriteStation favoriteStation = favoriteStationDao.save(FavoriteStation.builder()
                .owner(user.getId())
                .stationId(station.getId())
                .build());

        return FavoriteStationResponse.builder()
                .id(favoriteStation.getId())
                .owner(user.getId())
                .station(station).build();
    }

    public List<FavoriteStationResponse> findAll(final String email) {
        User user = userDao.findByEmail(email);

        List<FavoriteStationResponse> responses = new ArrayList<>();
        List<FavoriteStation> favoriteStations = favoriteStationDao.findAllByOwner(user.getId());

        for (FavoriteStation favoriteStation : favoriteStations) {
            Station station = stationDao.findById(favoriteStation.getStationId());

            responses.add(FavoriteStationResponse.builder()
                    .id(favoriteStation.getId())
                    .owner(favoriteStation.getOwner())
                    .station(station).build()
            );
        }

        return responses;
    }

    public void deleteByIdAndOwner(final String email, final long id) {
        User user = userDao.findByEmail(email);

        favoriteStationDao.deleteByIdAndOwner(id, user.getId());
    }
}
