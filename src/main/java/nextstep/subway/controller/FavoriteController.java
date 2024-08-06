package nextstep.subway.controller;

import lombok.RequiredArgsConstructor;
import nextstep.configuration.auth.AuthenticationPrincipal;
import nextstep.auth.domain.entity.LoginMember;
import nextstep.subway.controller.dto.CreateFavoriteRequest;
import nextstep.subway.domain.command.FavoriteCommand;
import nextstep.subway.domain.command.FavoriteCommander;
import nextstep.subway.domain.query.FavoriteReader;
import nextstep.subway.domain.view.FavoriteView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteCommander favoriteCommander;
    private final FavoriteReader favoriteReader;

    @PostMapping("")
    public ResponseEntity<FavoriteView.Main> createFavorite(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody CreateFavoriteRequest request
    ) {
        Long id = favoriteCommander.createFavorite(request.toCommand(loginMember.getId()));
        FavoriteView.Main view = favoriteReader.getOneById(id);

        return ResponseEntity
                .created(URI.create("/favorites/" + id))
                .body(view);
    }

    @GetMapping("")
    public ResponseEntity<List<FavoriteView.Main>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteView.Main> favorites = favoriteReader.getFavoritesByMemberId(loginMember.getId());
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long id
    ) {
        favoriteCommander.deleteFavorite(new FavoriteCommand.DeleteFavorite(loginMember.getId(), id));
        return ResponseEntity.noContent().build();
    }
}
