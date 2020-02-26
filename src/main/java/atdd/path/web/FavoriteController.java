package atdd.path.web;

import atdd.path.application.FavoritePathService;
import atdd.path.application.FavoriteStationService;
import atdd.path.application.dto.FavoritePathRequestView;
import atdd.path.application.dto.FavoritePathResponseView;
import atdd.path.application.dto.FavoriteStationResponse;
import atdd.path.domain.FavoritePath;
import atdd.user.web.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    @Autowired
    private FavoriteStationService favoriteStationService;

    @Autowired
    private FavoritePathService favoritePathService;

    @PostMapping("/stations")
    public ResponseEntity addFavoriteStation(@LoginUser final String email, @RequestBody final long stationId) {
        FavoriteStationResponse response = favoriteStationService.addFavoriteStation(email, stationId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/stations")
    public ResponseEntity findFavoriteStations(@LoginUser final String email) {
        List<FavoriteStationResponse> responses = favoriteStationService.findAll(email);

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity deleteFavoriteStation(@LoginUser final String email, @PathVariable Long id) {
        favoriteStationService.deleteByIdAndOwner(email, id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/paths")
    public ResponseEntity addFavoritePath(@LoginUser final String email, @RequestBody FavoritePathRequestView view) {
        FavoritePath favoritePath = favoritePathService.addFavoritePath(email, FavoritePath.builder()
                .sourceStationId(view.getSourceStationId())
                .targetStationId(view.getTargetStationId()).build());

        return ResponseEntity.status(HttpStatus.CREATED).body(FavoritePathResponseView.of(favoritePath));
    }

    @GetMapping("/paths")
    public ResponseEntity findFavoritePaths(@LoginUser final String email) {
        List<FavoritePath> favoritePaths = favoritePathService.findFavoritePath(email);

        List<FavoritePathResponseView> result = new ArrayList<>();

        for (FavoritePath favoritePath : favoritePaths) {
            result.add(FavoritePathResponseView.of(favoritePath));
        }
        
        return ResponseEntity.ok(result);
    }
}
