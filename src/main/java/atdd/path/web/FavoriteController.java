package atdd.path.web;

import atdd.path.application.FavoriteService;
import atdd.path.application.dto.FavoriteRequestView;
import atdd.path.application.dto.FavoriteResponseView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping("favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/station")
    public ResponseEntity createStationFavorite(FavoriteRequestView favoriteRequestView, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        FavoriteResponseView response = favoriteService.createStationFavorite(token, favoriteRequestView.getStationId());

        return ResponseEntity.created(URI.create("/favorite/" + 1))
                .body(response);
    }
}
