package nextstep.member.application;

import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.Member;
import nextstep.member.domain.exception.NotFoundException;
import nextstep.member.repository.FavoriteRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
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

    @Transactional
    public FavoriteResponse create(String principal, FavoriteRequest favoriteRequest) {
        Member member = memberService.findByPrincipal(principal);
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());

        Favorite favorite = member.addFavorite(source, target);
        favoriteRepository.save(favorite);
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findAll(String principal) {
        return memberService.findByPrincipal(principal)
            .getFavorites()
            .stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void delete(String principal, Long id) {
        Member member = memberService.findByPrincipal(principal);
        Favorite favorite = favoriteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(id + "번 즐겨찾기를 찾을 수 없습니다."));

        member.deleteFavorite(favorite);
    }
}
