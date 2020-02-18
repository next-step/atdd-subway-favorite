package atdd.path.web;

import atdd.path.application.dto.FavoriteStationResponseView;
import atdd.path.application.resolver.LoginUser;
import atdd.path.domain.FavoriteStation;
import atdd.path.domain.Member;
import atdd.path.domain.Station;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

    @PostMapping("/stations/{id}")
    public ResponseEntity<FavoriteStationResponseView> createFavoriteStation(@PathVariable("id") Long stationId,
                                                                             @LoginUser Member member) {

        final FavoriteStation savedFavoriteStation = new FavoriteStation(1L, new Station(stationId, "강남역"));

        return ResponseEntity.created(URI.create("/favorites/"+ savedFavoriteStation.getId()))
                .body(new FavoriteStationResponseView(savedFavoriteStation));
    }

}