package nextstep.subway.ui;

import nextstep.auth.model.authorization.AuthenticationPrincipal;
import nextstep.subway.application.FavouriteService;
import nextstep.subway.application.dto.FavouriteRequest;
import nextstep.subway.application.dto.FavouriteResponse;
import nextstep.subway.domain.member.MemberAdaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favourites")
public class FavouriteController {
    private final FavouriteService favouriteService;

    public FavouriteController(FavouriteService favouriteService) {
        this.favouriteService = favouriteService;
    }

    @PostMapping
    public ResponseEntity doFavourite(@AuthenticationPrincipal MemberAdaptor memberAdaptor,
                                      @RequestBody FavouriteRequest favouriteRequest) {
        if (memberAdaptor.isEnabled()) {
            Long favouriteId = favouriteService.add(memberAdaptor.getId(), favouriteRequest);
            return ResponseEntity.created(URI.create("/favourites/" + favouriteId)).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping
    public ResponseEntity<List<FavouriteResponse>> getFavouriteList(@AuthenticationPrincipal MemberAdaptor memberAdaptor) {
        if (memberAdaptor.isEnabled()) {
            return ResponseEntity.ok().body(favouriteService.findAll(memberAdaptor.getId()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @DeleteMapping("/{favouriteId}")
    public ResponseEntity<Void> deleteFavourite(@AuthenticationPrincipal MemberAdaptor memberAdaptor,
                                                @PathVariable("favouriteId") Long favouriteId) {
        if (memberAdaptor.isEnabled()) {
            favouriteService.delete(memberAdaptor.getId(), favouriteId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
