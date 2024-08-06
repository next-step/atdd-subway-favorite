package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.exception.NotExistFavoriteException;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.line.domain.Section;
import nextstep.line.domain.SectionRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.path.domain.ShortestPath;
import nextstep.station.application.StationService;
import nextstep.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final SectionRepository sectionRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, SectionRepository sectionRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    @Transactional
    public FavoriteResponse createFavorite(String memberEmail, FavoriteRequest favoriteRequest) {
        Station start = stationService.lookUp(favoriteRequest.getSource());
        Station end = stationService.lookUp(favoriteRequest.getTarget());

        List<Section> sections = sectionRepository.findAll();
        ShortestPath path = ShortestPath.from(sections);

        path.validateContains(start, end);
        path.validateConnected(start, end);

        Member member = memberService.findMemberByEmail(memberEmail);
        Favorite favorite = favoriteRepository.save(new Favorite(start, end, member));
        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> findFavorites(String memberEmail) {
        Member member = memberService.findMemberByEmail(memberEmail);

        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
        return createFavoriteResponses(favorites);
    }

    private List<FavoriteResponse> createFavoriteResponses(List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long id, String memberEmail) {
        Member member = memberService.findMemberByEmail(memberEmail);
        favoriteRepository.findByIdAndMemberId(id, member.getId())
                .orElseThrow(NotExistFavoriteException::new);
        favoriteRepository.deleteById(id);
    }
}
