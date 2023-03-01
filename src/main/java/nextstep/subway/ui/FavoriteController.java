package nextstep.subway.ui;

import nextstep.member.ui.LoginMember;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private FavoriteRepository favoriteRepository;
    private StationService stationService;

    public FavoriteController(FavoriteRepository favoriteRepository, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    @PostMapping
    public ResponseEntity<Void> createLine(LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest) {
        Station sourceStation = stationService.findById(favoriteRequest.getSource());
        Station targetStation = stationService.findById(favoriteRequest.getTarget());

        Favorite favorite = favoriteRepository.save(new Favorite(loginMember.getId(), sourceStation, targetStation));
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }
}
