package nextstep.subway.ui;

import nextstep.member.ui.LoginMember;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest) {
        Long memberId = loginMember.getId();
        Long sourceStationId = favoriteRequest.getSource();
        Long targetStationId = favoriteRequest.getTarget();

        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(memberId, sourceStationId, targetStationId);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> readLine(LoginMember loginMember) {
        List<FavoriteResponse> myFavorites = favoriteService.findByMember(loginMember.getId());
        return ResponseEntity.ok(myFavorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLine(LoginMember loginMember, @PathVariable Long id) {
        favoriteService.deleteFavorite(id, loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
