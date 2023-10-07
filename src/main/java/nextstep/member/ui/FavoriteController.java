package nextstep.member.ui;

import nextstep.member.application.dto.CreateFavoriteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class FavoriteController {

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorites(@RequestBody CreateFavoriteRequest request) {
        return ResponseEntity.created(URI.create("/favorites/1")).build();
    }
}
