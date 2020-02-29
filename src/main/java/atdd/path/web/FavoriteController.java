package atdd.path.web;

import atdd.path.application.FavoriteService;
import atdd.path.application.dto.favorite.FavoriteCreateRequestView;
import atdd.path.application.dto.favorite.FavoriteCreateResponseView;
import atdd.path.application.dto.favorite.FavoriteListResponseView;
import atdd.path.domain.User;
import atdd.path.security.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("")
    public ResponseEntity create(@LoginUser User user, @RequestBody FavoriteCreateRequestView favorite) {
        FavoriteCreateResponseView savedFavorite = favoriteService.save(user, favorite);
        return ResponseEntity.created(URI.create("/favorites/" + savedFavorite.getId())).body(savedFavorite);
    }

    @GetMapping("")
    public ResponseEntity findByUser(@LoginUser User user, @RequestParam String type) {
        FavoriteListResponseView favorites = favoriteService.findByUser(user, type);
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("{stationId}")
    public ResponseEntity deleteStation(@LoginUser User user, @PathVariable Long stationId) {
        favoriteService.deleteStation(user, stationId);
        return ResponseEntity.ok().build();
    }
}
