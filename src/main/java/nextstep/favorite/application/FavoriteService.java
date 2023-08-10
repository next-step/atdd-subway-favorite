package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.repository.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(String username, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMemberByEmail(username);
        Station source = stationService.getStations(favoriteRequest.getSource());
        Station target = stationService.getStations(favoriteRequest.getTarget());
        Favorite favorite = favoriteRepository.save(Favorite.builder()
                .member(member)
                .source(source)
                .target(target)
                .build());

        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorite(String email) {
        Member member = memberService.findMemberByEmail(email);
        return favoriteRepository.findByMember(member)
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorites(String email, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(RuntimeException::new);
        Member member = memberService.findMemberByEmail(email);
        if(!member.equals(favorite.getMember())){
            throw new RuntimeException("회원 정보가 올바르지 않습니다.");
        }

        favoriteRepository.delete(favorite);
    }
}
