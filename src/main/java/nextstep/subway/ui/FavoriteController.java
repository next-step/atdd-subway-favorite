package nextstep.subway.ui;

import io.jsonwebtoken.Header;
import lombok.RequiredArgsConstructor;
import nextstep.config.annotation.AuthHeader;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteCreateRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid FavoriteCreateRequest request, @AuthHeader String header) {
        Favorite favorite = favoriteService.save(request, header);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping("{id}")
    public void get(@PathVariable Long id) {

    }
}

