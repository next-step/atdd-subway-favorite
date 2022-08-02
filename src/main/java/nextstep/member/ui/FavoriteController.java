package nextstep.member.ui;

import nextstep.auth.authentication.AuthenticationPrincipal;
import nextstep.auth.service.LoginMember;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> addFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest) {
        favoriteService.createFavorite(loginMember.getEmail(), favoriteRequest.getSource(), favoriteRequest.getTarget());
        return ResponseEntity.ok().build();
    }

}