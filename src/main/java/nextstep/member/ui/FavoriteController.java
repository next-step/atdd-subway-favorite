package nextstep.member.ui;

import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.auth.Auth;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> addFavorite(@Auth LoginMember loginMember, @RequestBody FavoriteRequest request) {
        Favorite favorite = favoriteService.save(loginMember, request.getSource(), request.getTarget());

        return ResponseEntity
                .created(URI.create("/favorites/" + favorite.getId()))
                .build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findAll(@Auth LoginMember loginMember) {
        List<Favorite> favorites = favoriteService.findAll(loginMember).values();
        return ResponseEntity.ok().body(FavoriteResponse.asList(favorites));
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> delete(@Auth LoginMember loginMember, @PathVariable Long id) {
        favoriteService.delete(loginMember, id);
        return ResponseEntity.noContent().build();
    }


}
