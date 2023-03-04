package nextstep.favorites.ui;

import nextstep.favorites.application.FavoriteService;
import nextstep.favorites.application.dto.FavoriteRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public void addFavorites(@RequestBody FavoriteRequest favoriteRequest) {
        
    }
}
