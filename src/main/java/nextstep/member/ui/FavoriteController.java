package nextstep.member.ui;

import nextstep.auth.LoginMember;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @LoginMember
    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorites(@RequestBody FavoriteRequest request, Member member) {
        FavoriteResponse favorite = favoriteService.createFavorites(request, member);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @LoginMember
    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> selectFavorites(Member member) {
        List<FavoriteResponse> favorites = favoriteService.selectFavorites(member);
        return ResponseEntity.ok().body(favorites);
    }

    @LoginMember
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorites(@PathVariable Long id, Member member) {
        favoriteService.deleteFavorites(id, member);
        return ResponseEntity.noContent().build();
    }
}
