package atdd.path.application;

import atdd.path.application.exception.BadRequestException;
import atdd.path.application.exception.ConflictException;
import atdd.path.dao.FavoriteDao;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public FavoriteStation saveForStation(Member member, Long stationId) {
        final Station findStation = stationDao.findById(stationId);
        return favoriteDao.saveForStation(new FavoriteStation(member, findStation));
    }

    public FavoritePath saveForPath(Member member, Long startId, Long endId) {
        final Station sourceStation = stationDao.findById(startId);
        final Station targetStation = stationDao.findById(endId);

        if (sourceStation.isSameStation(targetStation)) {
            throw new ConflictException("same station conflict");
        }

        if (!isPathExists(sourceStation.getId(), targetStation.getId())) {
            throw new BadRequestException("no exist path");
        }

        return favoriteDao.saveForPath(new FavoritePath(member, sourceStation, targetStation));
    }

    private boolean isPathExists(Long sourceStationId, Long targetStationId) {
        final List<Line> lines = lineDao.findAll();
        final Graph graph = new Graph(lines);
        return graph.isPathExists(sourceStationId, targetStationId);
    }

    public List<FavoriteStation> findForStations(Member member) {
        return favoriteDao.findForStations(member);
    }

    public void deleteForStationById(Long favoriteId) {
        favoriteDao.deleteForStationById(favoriteId);
    }

    public List<FavoritePath> findForPaths(Member member) {
        return favoriteDao.findForPaths(member);
    }

    public void deleteForPathById(Long favoriteId) {
        favoriteDao.deleteForPathById(favoriteId);
    }

}