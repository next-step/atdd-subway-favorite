package nextstep.subway.ui;

import nextstep.member.domain.Member;
import nextstep.member.exception.UnAuthorizedException;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> addFavorite(
            final HttpServletRequest request,
            @RequestBody final FavoriteRequest favoriteRequest) {
        final Member member = (Member) request.getAttribute("user");
        if (Objects.isNull(member)) {
            throw new UnAuthorizedException();
        }
        final Long favoriteId = favoriteService.saveFavorite(member.getEmail(), favoriteRequest);

        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }
}
