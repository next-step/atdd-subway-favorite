package atdd.path.web;

import atdd.path.application.FavoriteService;
import atdd.path.application.dto.FavoriteRouteRequestView;
import atdd.path.application.dto.FavoriteRouteResponseView;
import atdd.path.application.dto.FavoriteStationRequestView;
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

    @PostMapping("/stations")
    public ResponseEntity createFavoriteStation(@RequestBody FavoriteStationRequestView favoriteStationRequestView, @LoginUser User user) {
        FavoriteResponseView response = favoriteService.createStationFavorite(favoriteStationRequestView.getStationId(), user);

        return ResponseEntity.created(URI.create("/favorite/" + response.getId()))
                .body(response);
    }

    @GetMapping("/stations")
    public ResponseEntity findFavoriteStation(@LoginUser User user) {
        return ResponseEntity.ok().body(favoriteService.findFavoriteStation(user));
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity deleteFavoriteStation(@LoginUser User user, @PathVariable Long id) {
        favoriteService.deleteFavoriteStation(user, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/routes")
    public ResponseEntity createFavoriteRoute(@RequestBody FavoriteRouteRequestView requestView, @LoginUser User user) {
        FavoriteRouteResponseView response = favoriteService.createRouteFavorite(requestView, user);
        return ResponseEntity.created(URI.create("/route/" + response.getId())).body(response);
    }

    @GetMapping("/routes")
    public ResponseEntity findFavoriteRoute(@LoginUser User user) {
        return ResponseEntity.ok().body(favoriteService.findFavoriteRoute(user));
    }

    @DeleteMapping("/routes/{id}")
    public ResponseEntity deleteFavoriteRoute(@PathVariable Long id, @LoginUser User user) {
        return ResponseEntity.noContent().build();
    }
}
