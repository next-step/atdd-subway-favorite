package nextstep.subway.ui;

import nextstep.common.auth.LoginRequired;
import nextstep.common.auth.MemberPayload;
import nextstep.common.auth.VerifiedMember;
import nextstep.subway.applicaion.FavoriteService;
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

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    @LoginRequired
    ResponseEntity<Void> createFavorite(
            @VerifiedMember final MemberPayload memberPayload,
            @RequestBody final FavoriteCreateRequest favoriteCreateRequest
    ) {
        Long favoriteId = favoriteService.createFavorite(memberPayload.getEmail(), favoriteCreateRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping
    @LoginRequired
    ResponseEntity<List<FavoriteResponse>> showFavorites(@VerifiedMember final MemberPayload memberPayload) {
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavoriteResponses(memberPayload.getEmail());
        return ResponseEntity.ok().body(favoriteResponses);
    }
}
