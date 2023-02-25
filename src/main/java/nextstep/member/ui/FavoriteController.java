package nextstep.member.ui;

import nextstep.auth.config.AuthRequest;
import nextstep.member.application.FavoriteService;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.FarvoriteRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteController(FavoriteService favoriteService, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteService = favoriteService;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @PostMapping("/favorite")
    public ResponseEntity<Void> createMember(@AuthRequest String email, @RequestBody FarvoriteRequest param) {
        Member member = memberRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        Station sourceStation = stationRepository.findById(param.getSource()).orElseThrow(IllegalArgumentException::new);
        Station targetStation = stationRepository.findById(param.getTarget()).orElseThrow(IllegalArgumentException::new);

        favoriteService.save(Favorite.builder()
                .member(member)
                .sourceStation(sourceStation)
                .targetStation(targetStation).build());

        return ResponseEntity.created(URI.create("/favorite/")).build();
    }
}
