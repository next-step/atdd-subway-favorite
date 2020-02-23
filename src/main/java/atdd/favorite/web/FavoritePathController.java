package atdd.favorite.web;

import atdd.favorite.application.FavoritePathService;
import atdd.favorite.application.dto.CreateFavoritePathRequestView;
import atdd.favorite.application.dto.FavoritePathResponseView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

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

}
