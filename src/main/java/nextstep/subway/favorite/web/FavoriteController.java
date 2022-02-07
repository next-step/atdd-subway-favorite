package nextstep.subway.favorite.web;

import lombok.RequiredArgsConstructor;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/favorites")
@Controller
public class FavoriteController {

    @PostMapping
    public ResponseEntity<Void> createFavorite(@RequestBody FavoriteRequest request) {
        return ResponseEntity.created(URI.create("/favorites/")).build();
    }
}
