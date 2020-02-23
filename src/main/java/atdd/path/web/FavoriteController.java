package atdd.path.web;

import atdd.path.application.dto.favorite.FavoriteCreateRequestView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    public FavoriteController() {
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody FavoriteCreateRequestView favorite) {
        return ResponseEntity
                .created(URI.create("/favorites/1"))
                .body(favorite.getName());
    }
}
