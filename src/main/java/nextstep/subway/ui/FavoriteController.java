package nextstep.subway.ui;

import nextstep.member.domain.auth.AuthenticationPrincipal;
import nextstep.member.domain.auth.LoginMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest request) {
        FavoriteResponse favorite = favoriteService.saveFavorite(loginMember.getEmail(), request.getDepartureStationId(), request.getDestinationStationId());
        System.out.println("favorite = " + favorite);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> showFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(loginMember.getEmail());
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<FavoriteResponse>> deleteFavorites(@AuthenticationPrincipal LoginMember loginMember, @PathVariable("id") Long favoriteId) {
        favoriteService.deleteFavorite(loginMember.getEmail(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
