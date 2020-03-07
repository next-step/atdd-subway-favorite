package atdd.path.web;

import atdd.path.application.FavoriteService;
import atdd.path.application.dto.FavoritePathResponseView;
import atdd.path.application.dto.FavoriteStationResponseView;
import atdd.path.domain.User;
import atdd.path.security.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static atdd.path.application.base.BaseUriConstants.*;

@RestController
@RequestMapping(FAVORITE_BASE_URL)
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping(value = STATION_BASE_URL + "/{id}")
    public ResponseEntity<FavoriteStationResponseView> createFavoriteStation(@PathVariable(name = "id") Long stationId,
                                                                             @LoginUser User user) {
        FavoriteStationResponseView favoriteStation = favoriteService.saveFavoriteStation(stationId, user);
        return ResponseEntity.created(URI.create(FAVORITE_BASE_URL + STATION_BASE_URL + "/" + favoriteStation.getId()))
                .body(favoriteStation);
    }

    @GetMapping(value = STATION_BASE_URL + "/{id}")
    public ResponseEntity<FavoriteStationResponseView> retrieveFavoriteStation(@PathVariable Long id,
                                                                               @LoginUser User user) {
        FavoriteStationResponseView favoriteStation = favoriteService.findFavoriteStation(id, user);
        return ResponseEntity.ok().body(favoriteStation);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteStationResponseView>> showFavoriteStations(@LoginUser User user) {
        List<FavoriteStationResponseView> favoriteStations = favoriteService.findFavoriteStations(user);
        return ResponseEntity.ok().body(favoriteStations);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity deleteFavoriteStation(@PathVariable Long id, @LoginUser User user) {
        favoriteService.deleteFavoriteStation(id, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = PATH_BASE_URL)
    public ResponseEntity<FavoritePathResponseView> createFavoriteStationPath(@RequestParam Long startId,
                                                                              @RequestParam Long endId,
                                                                              @LoginUser User user) {
        FavoritePathResponseView favoritePath = favoriteService.saveFavoritePath(startId, endId, user);
        return ResponseEntity.created(URI.create(FAVORITE_BASE_URL + PATH_BASE_URL + "/" + favoritePath.getId()))
                .body(favoritePath);
    }

    @GetMapping(value = PATH_BASE_URL)
    public ResponseEntity<List<FavoritePathResponseView>> showFavoritePaths(@LoginUser User user) {
        List<FavoritePathResponseView> favoritePaths = favoriteService.findFavoritePaths(user);
        return ResponseEntity.ok().body(favoritePaths);
    }

    @DeleteMapping(value = PATH_BASE_URL + "/{id}")
    public ResponseEntity deleteFavoritePath(@PathVariable Long id, @LoginUser User user) {
        favoriteService.deleteFavoritePath(id, user);
        return ResponseEntity.noContent().build();
    }
}
