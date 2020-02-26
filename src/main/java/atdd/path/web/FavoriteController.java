package atdd.path.web;

import atdd.path.application.dto.favorite.FavoriteCreateRequestView;
import atdd.path.dao.FavoriteDao;
import atdd.path.domain.Favorite;
import atdd.path.domain.User;
import atdd.path.security.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private FavoriteDao favoriteDao;

    public FavoriteController(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    @PostMapping("")
    public ResponseEntity create(@LoginUser User user, @RequestBody FavoriteCreateRequestView favorite) {
        Favorite savedFavorite = favoriteDao.save(favorite.toEntity());
        return ResponseEntity.created(URI.create("/favorites/" + savedFavorite.getId())).body(savedFavorite);
    }

}
