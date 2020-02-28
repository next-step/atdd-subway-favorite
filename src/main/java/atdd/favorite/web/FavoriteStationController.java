package atdd.favorite.web;

import atdd.favorite.FavoriteConstant;
import atdd.favorite.application.dto.CreateFavoriteStationRequestView;
import atdd.favorite.application.dto.LoginUser;
import atdd.user.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static atdd.favorite.FavoriteConstant.*;

@RestController
@RequestMapping(FAVORITE_STATION_BASE_URI)
public class FavoriteStationController {

    @PostMapping
    public ResponseEntity create(@LoginUser User user,
                                 CreateFavoriteStationRequestView requestView){
        return ResponseEntity.created(URI.create(FAVORITE_STATION_BASE_URI+"/"+1))
                .body(user);
    }
}
