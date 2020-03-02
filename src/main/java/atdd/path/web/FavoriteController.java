package atdd.path.web;

import atdd.path.application.FavoriteService;
import atdd.path.application.dto.FavoriteStationResponseView;
import atdd.path.domain.FavoriteStation;
import atdd.path.domain.User;
import atdd.path.security.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static atdd.path.application.base.BaseUriConstants.FAVORITE_BASE_URL;
import static atdd.path.application.base.BaseUriConstants.STATION_BASE_URL;

@RestController
@RequestMapping(FAVORITE_BASE_URL)
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping(value = "/stations/{id}")
    public ResponseEntity<FavoriteStationResponseView> createFavoriteStation(@PathVariable(name = "id") Long stationId,
                                                                             @LoginUser User user) {
        FavoriteStationResponseView favoriteStation = favoriteService.saveFavoriteStation(stationId, user);
        return ResponseEntity.created(URI.create(FAVORITE_BASE_URL + STATION_BASE_URL + "/" + favoriteStation.getId()))
                .body(favoriteStation);
    }

    @GetMapping(value = "/stations/{id}")
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
}
