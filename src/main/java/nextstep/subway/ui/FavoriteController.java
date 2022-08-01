package nextstep.subway.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.member.domain.LoginMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> addFavorite(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse favorite = favoriteService.addFavorite(loginMember.getId(), favoriteRequest.getSource(), favoriteRequest.getTarget());
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }
}
