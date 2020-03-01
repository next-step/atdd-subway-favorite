package atdd.path.web;

import atdd.path.application.FavoriteService;
import atdd.path.application.base.BaseUriConstants;
import atdd.path.application.dto.FavoriteStationResponseView;
import atdd.path.dao.FavoriteDao;
import atdd.path.domain.FavoriteStation;
import atdd.path.domain.User;
import atdd.path.security.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
        FavoriteStation favoriteStation = favoriteService.saveFavoriteStation(stationId, user);
        return ResponseEntity.created(URI.create(FAVORITE_BASE_URL + STATION_BASE_URL + "/" + favoriteStation.getId()))
                .body(FavoriteStationResponseView.of(favoriteStation));
    }
}
