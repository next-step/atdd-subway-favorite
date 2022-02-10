package nextstep.favorite.ui;

import java.net.URI;
import java.util.List;
import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.dto.FavoriteRequest;
import nextstep.favorite.dto.FavoriteResponse;
import nextstep.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<FavoriteResponse> createFavorite(
        @AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest request)
    {
        FavoriteResponse response = favoriteService.saveFavorite(request);
        return ResponseEntity.created(URI.create("/favorites" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findAllFavorite(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> responseList = favoriteService.findAllFavorite();
        return ResponseEntity.ok().body(responseList);
    }

}
