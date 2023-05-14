package nextstep.favorites.ui;

import lombok.RequiredArgsConstructor;
import nextstep.config.data.UserSession;
import nextstep.favorites.application.FavoriteService;
import nextstep.favorites.application.dto.FavoriteRequest;
import nextstep.favorites.application.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<FavoriteResponse> post(@RequestBody FavoriteRequest favoriteRequest, UserSession userSession) {
        FavoriteResponse response = favoriteService.save(favoriteRequest, userSession.getId());
        return ResponseEntity.created(URI.create("/favorite/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getMyFavorites(UserSession userSession) {
        List<FavoriteResponse> list = favoriteService.getFavorites(userSession.getId());
        return ResponseEntity.ok().body(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id, UserSession userSession) {
        favoriteService.delete(id, userSession.getId());
        return ResponseEntity.noContent().build();
    }

}
