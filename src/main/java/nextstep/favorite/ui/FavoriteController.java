package nextstep.favorite.ui;

import java.util.List;
import nextstep.auth.principal.AuthenticationPrincipal;
import nextstep.auth.principal.UserPrincipal;
import nextstep.favorite.application.FavoriteCreateRequest;
import nextstep.favorite.application.FavoriteDao;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.domain.FavoriteResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final FavoriteDao favoriteDao;

    public FavoriteController(FavoriteService favoriteService, FavoriteDao favoriteDao) {
        this.favoriteService = favoriteService;
        this.favoriteDao = favoriteDao;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> create(@AuthenticationPrincipal UserPrincipal user,
            @RequestBody FavoriteCreateRequest favoriteCreateRequest) {
        long source = favoriteCreateRequest.getSource();
        long target = favoriteCreateRequest.getTarget();
        String email = user.getUsername();
        long favoriteId = favoriteService.create(email, source, target);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, "/favorites/" + favoriteId)
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getAll(@AuthenticationPrincipal UserPrincipal user) {
        String email = user.getUsername();
        List<FavoriteResponse> favoriteResponses = favoriteDao.getAll(email);
        return ResponseEntity.ok().body(favoriteResponses);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserPrincipal user, @PathVariable Long id) {
        String email = user.getUsername();
        favoriteService.delete(email, id);
        return ResponseEntity.noContent().build();
    }
}
