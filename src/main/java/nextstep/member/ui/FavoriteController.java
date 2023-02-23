package nextstep.member.ui;

import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/favorite")
    public ResponseEntity<Void> createMember() {
        favoriteService.save(Favorite.builder()
                .member(new Member("email@email.com","password",10))
                .sourceStation(new Station("station1"))
                .targetStation(new Station("station2")).build());

        return ResponseEntity.created(URI.create("/favorite/")).build();
    }
}
