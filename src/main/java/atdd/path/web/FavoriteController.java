package atdd.path.web;

import atdd.path.application.FavoriteService;
import atdd.path.application.dto.favorite.FavoriteCreateRequestView;
import atdd.path.domain.Favorite;
import atdd.path.domain.Station;
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
        Favorite savedFavorite = favoriteService.save(user, favorite);
        return ResponseEntity.created(URI.create("/favorites/" + savedFavorite.getId())).body(savedFavorite);
    }

    @GetMapping("")
    public ResponseEntity detail(@LoginUser User user) {
        return ResponseEntity.ok(new Favorite(user, new Station("강남역")));
    }
}
