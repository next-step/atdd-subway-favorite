package atdd.path.web;

import atdd.path.application.FavoriteService;
import atdd.path.application.dto.FavoriteRequestView;
import atdd.path.application.dto.FavoriteResponseView;
import atdd.user.domain.User;
import atdd.user.web.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/station")
    public ResponseEntity createStationFavorite(FavoriteRequestView favoriteRequestView, @LoginUser User user) {
        FavoriteResponseView response = favoriteService.createStationFavorite(favoriteRequestView.getStationId(), user);

        return ResponseEntity.created(URI.create("/favorite/" + 1))
                .body(response);
    }

    @GetMapping("/station")
    public ResponseEntity findStationFavorite(@LoginUser User user) {
        return ResponseEntity.ok().build();
    }
}
