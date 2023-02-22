package nextstep.subway.ui;

import nextstep.common.ui.BaseController;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController extends BaseController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> save(@RequestBody FavoriteRequest favoriteRequest, HttpServletRequest request) {
        String email = getPrincipal(request);
        FavoriteResponse favoriteResponse = favoriteService.save(email, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getAll(HttpServletRequest request) {
        String email = getPrincipal(request);
        List<FavoriteResponse> responses = favoriteService.findFavoriteResponses(email);
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteResponse> get(@PathVariable Long id, HttpServletRequest request) {
        String email = getPrincipal(request);
        FavoriteResponse favoriteResponse = favoriteService.findFavoriteResponseById(email, id);
        return ResponseEntity.ok().body(favoriteResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        String email = getPrincipal(request);
        favoriteService.delete(email, id);
        return ResponseEntity.noContent().build();
    }
}
