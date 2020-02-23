package atdd.favorite.web;

import atdd.favorite.application.FavoritePathService;
import atdd.favorite.application.dto.CreateFavoritePathRequestView;
import atdd.favorite.application.dto.FavoritePathListResponseView;
import atdd.favorite.application.dto.FavoritePathResponseView;
import atdd.favorite.domain.FavoritePath;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

import static atdd.Constant.FAVORITE_PATH_BASE_URI;

@RestController
@RequestMapping(FAVORITE_PATH_BASE_URI)
public class FavoritePathController {
    private FavoritePathService service;

    private FavoritePathController(FavoritePathService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FavoritePathResponseView> createFavoritePath(@RequestBody CreateFavoritePathRequestView request,
                                                                       HttpServletRequest httpServletRequest) {
        String email = (String) httpServletRequest.getAttribute("email");
        request.insertUserEmail(email);
        FavoritePathResponseView response = service.create(request);
        return ResponseEntity
                .created(URI.create(FAVORITE_PATH_BASE_URI + "/" + response.getId()))
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavoritePath(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping
    public ResponseEntity<FavoritePathListResponseView> showFavoritePaths(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        List<FavoritePath> favoritePaths = service.findAllByEmail(email);
        FavoritePathListResponseView responseView
                = new FavoritePathListResponseView(email, favoritePaths);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseView);
    }
}
