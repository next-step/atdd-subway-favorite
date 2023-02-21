package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse findFavoriteResponseById(Long id) {
        Favorite favorite = findById(id);
        return FavoriteResponse.toResponse(favorite);
    }

    public Favorite findById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<FavoriteResponse> findFavoriteResponses(String email) {
        Member member = memberService.findByEmail(email);
        return favoriteRepository.findByMember(member).stream()
                .map(FavoriteResponse::toResponse)
                .collect(Collectors.toList());
    }

    public FavoriteResponse save(String email, FavoriteRequest favoriteRequest) {
        Member member = memberService.findByEmail(email);
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        return FavoriteResponse.toResponse(favoriteRepository.save(new Favorite(member, source, target)));
    }

    public void delete(Long id) {
        favoriteRepository.deleteById(id);
    }
}
