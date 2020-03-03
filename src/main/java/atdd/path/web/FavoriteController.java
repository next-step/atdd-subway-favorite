package atdd.path.web;

import atdd.path.application.FavoriteService;
import atdd.path.application.dto.FavoriteRequestView;
import atdd.path.application.dto.FavoriteResponseView;
import atdd.user.domain.User;
import atdd.user.web.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/station")
    public ResponseEntity createFavoriteStation(@RequestBody FavoriteRequestView favoriteRequestView, @LoginUser User user) {
        FavoriteResponseView response = favoriteService.createStationFavorite(favoriteRequestView.getStationId(), user);

        return ResponseEntity.created(URI.create("/favorite/" + 1))
                .body(response);
    }

    @GetMapping("/station")
    public ResponseEntity findFavoriteStation(@LoginUser User user) {
        return ResponseEntity.ok().body(favoriteService.findFavoriteStation(user));
    }

    @DeleteMapping("/station/{id}")
    public ResponseEntity deleteFavoriteStation(@LoginUser User user, @PathVariable Long id) {
        favoriteService.deleteFavoriteStation(user, id);
        return ResponseEntity.noContent().build();
    }
}
