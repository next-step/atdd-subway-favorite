package nextstep.favorite.ui;

import java.net.URI;
import nextstep.favorite.dto.FavoriteRequest;
import nextstep.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {



    @GetMapping
    public ResponseEntity<FavoriteResponse> createFavorite(@RequestBody FavoriteRequest request) {
//        FavoriteResponse response = favoriteService.saveFavorite(request);
//        return ResponseEntity.created(URI.create("/favorites" + response.getId())).body(response);
    }

}
