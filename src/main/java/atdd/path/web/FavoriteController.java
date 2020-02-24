package atdd.path.web;

import atdd.path.application.FavoriteStationService;
import atdd.path.domain.FavoriteStation;
import atdd.user.web.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    @Autowired
    private FavoriteStationService favoriteStationService;

    @PostMapping
    public ResponseEntity addFavoriteStation(@LoginUser final String email, @RequestBody final long stationId) {
        FavoriteStation favoriteStation = favoriteStationService.addFavoriteStation(email, stationId);

        return ResponseEntity.status(HttpStatus.CREATED).body(favoriteStation);
    }
}
