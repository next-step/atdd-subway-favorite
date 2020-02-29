package atdd.favorite.web;

import atdd.favorite.application.dto.FavoritePathRequestView;
import atdd.favorite.application.dto.FavoritePathResponseView;
import atdd.favorite.application.dto.LoginUser;
import atdd.favorite.service.FavoritePathService;
import atdd.user.domain.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static atdd.favorite.FavoriteConstant.FAVORITE_PATH_BASE_URI;

@RestController
@RequestMapping(FAVORITE_PATH_BASE_URI)
public class FavoritePathController {
    private FavoritePathService favoritePathService;

    public FavoritePathController(FavoritePathService favoritePathService) {
        this.favoritePathService = favoritePathService;
    }

    @PostMapping
    public ResponseEntity create(@LoginUser User user,
                                 @RequestBody FavoritePathRequestView requestView) throws Exception {
        requestView.insertEmail(user.getEmail());
        FavoritePathResponseView responseView = favoritePathService.create(requestView);
        return ResponseEntity
                .created(URI.create(FAVORITE_PATH_BASE_URI+"/"+responseView.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseView);
    }
}
