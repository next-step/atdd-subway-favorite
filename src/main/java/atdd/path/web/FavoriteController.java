package atdd.path.web;

import atdd.path.application.FavoriteService;
import atdd.path.application.dto.FavoriteStationResponseView;
import atdd.path.application.resolver.LoginUser;
import atdd.path.domain.FavoriteStation;
import atdd.path.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/stations/{id}")
    public ResponseEntity<FavoriteStationResponseView> createFavoriteStation(@PathVariable("id") Long stationId,
                                                                             @LoginUser Member member) {

        final FavoriteStation savedFavoriteStation = favoriteService.saveForStation(member, stationId);

        return ResponseEntity.created(URI.create("/favorites/"+ savedFavoriteStation.getId()))
                .body(new FavoriteStationResponseView(savedFavoriteStation));
    }

}