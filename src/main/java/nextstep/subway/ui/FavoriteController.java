package nextstep.subway.ui;

import nextstep.auth.secured.Secured;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Secured("ROLE_MEMBER")
    @PostMapping
    public ResponseEntity<Void> createFavorite(@RequestBody FavoriteRequest favoriteRequest) {
        return ResponseEntity.created(URI.create("/favorites/" + 1)).build();
    }

}
