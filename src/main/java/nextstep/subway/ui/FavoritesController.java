package nextstep.subway.ui;

import lombok.RequiredArgsConstructor;
import nextstep.auth.secured.Secured;
import nextstep.member.domain.RoleType;
import nextstep.subway.applicaion.FavoritesService;
import nextstep.subway.applicaion.dto.FavoriteCreateRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/favorites")
public class FavoritesController {
    private final FavoritesService favoritesService;

    @Secured(RoleType.ROLE_ADMIN)
    @PostMapping
    public ResponseEntity<Void> createFavorite(@RequestBody FavoriteCreateRequest request){
        FavoriteResponse favorite = favoritesService.createFavorite(request);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @Secured(RoleType.ROLE_ADMIN)
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(){
        List<FavoriteResponse> response = favoritesService.getFavorites();
        return ResponseEntity.ok(response);
    }
}
