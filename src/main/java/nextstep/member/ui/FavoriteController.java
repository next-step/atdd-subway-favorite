package nextstep.member.ui;

import nextstep.member.application.FavoriteService;
import nextstep.member.application.config.AuthMember;
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

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@AuthMember Member member, @RequestBody FavoriteRequest param) {
        FavoriteResponse response = favoriteService.createFavorite(member, param);
        return ResponseEntity.created(URI.create("/favorites/"+response.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> showFavorite(@AuthMember Member member) {
        List<FavoriteResponse> favoriteResponses = favoriteService.showFavorite(member);
        return ResponseEntity.ok().body(favoriteResponses);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthMember Member member, @PathVariable Long id) {
        favoriteService.deleteFavorite(member, id);
        return ResponseEntity.noContent().build();
    }

}
