package nextstep.subway.ui;

import java.net.URI;
import nextstep.member.ui.dto.LoginUser;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteCreateRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> create(LoginUser loginUser, @RequestBody FavoriteCreateRequest request) {
        final FavoriteResponse favoriteResponse = favoriteService.create(
            loginUser.getEmail(),
            request.getTarget(),
            request.getSource()
        );
        return ResponseEntity.created(URI.create("/favorites" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @GetMapping("{favoriteId}")
    public ResponseEntity<FavoriteResponse> findById(LoginUser loginUser, @PathVariable Long favoriteId) {
        final FavoriteResponse favoriteResponse = favoriteService.findById(
            favoriteId,
            loginUser.getEmail()
        );
        return ResponseEntity.ok(favoriteResponse);
    }

}
