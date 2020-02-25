package atdd.path.web;

import atdd.path.application.FavoriteStationService;
import atdd.path.application.dto.FavoriteStationResponse;
import atdd.user.web.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    @Autowired
    private FavoriteStationService favoriteStationService;

    @PostMapping
    public ResponseEntity addFavoriteStation(@LoginUser final String email, @RequestBody final long stationId) {
        FavoriteStationResponse response = favoriteStationService.addFavoriteStation(email, stationId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity findFavoriteStations(@LoginUser final String email) {
        List<FavoriteStationResponse> responses = favoriteStationService.findAll(email);

        return ResponseEntity.ok(responses);
    }
}
