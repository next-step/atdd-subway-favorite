package atdd.favorite.web;

import atdd.favorite.application.dto.FavoriteStationRequestView;
import atdd.favorite.application.dto.FavoriteStationResponseView;
import atdd.favorite.application.dto.LoginUser;
import atdd.favorite.service.FavoriteStationService;
import atdd.user.domain.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static atdd.favorite.FavoriteConstant.FAVORITE_STATION_BASE_URI;

@RestController
@RequestMapping(FAVORITE_STATION_BASE_URI)
public class FavoriteStationController {
    private FavoriteStationService favoriteStationService;

    public FavoriteStationController(FavoriteStationService favoriteStationService) {
        this.favoriteStationService = favoriteStationService;
    }

    @PostMapping
    public ResponseEntity create(@LoginUser User user,
                                 FavoriteStationRequestView requestView) {
        requestView.insertEmail(user.getEmail());
        FavoriteStationResponseView responseView = favoriteStationService.create(requestView);
        return ResponseEntity
                .created(URI.create(FAVORITE_STATION_BASE_URI + "/" + responseView.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseView);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@LoginUser User user, @PathVariable Long id,
                                 FavoriteStationRequestView requestView) throws Exception {
        requestView.insertEmail(user.getEmail());
        requestView.insertId(id);
        favoriteStationService.delete(requestView);
        return ResponseEntity
                .ok()
                .build();
    }
}
