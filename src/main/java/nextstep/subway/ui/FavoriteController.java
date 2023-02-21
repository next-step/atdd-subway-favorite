package nextstep.subway.ui;

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
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> save(@RequestBody FavoriteRequest favoriteRequest, HttpServletRequest request) {
        String email = (String) request.getAttribute("principal");
        FavoriteResponse favoriteResponse = favoriteService.save(email, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getAll(HttpServletRequest request) {
        String email = (String) request.getAttribute("principal");
        List<FavoriteResponse> responses = favoriteService.findFavoriteResponses(email);
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteResponse> get(@PathVariable Long id) {
        FavoriteResponse favoriteResponse = favoriteService.findFavoriteResponseById(id);
        return ResponseEntity.ok().body(favoriteResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        favoriteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
