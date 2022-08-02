package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.application.dto.FavoriteStationResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           StationService stationService,
                           MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public Long createFavorite(String principal, Long source, Long target) {
        Member findMember = findMemberByPrincipal(principal);

        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);

        Favorite favorite = favoriteRepository.save(new Favorite(findMember.getId(), sourceStation, targetStation));
        return favorite.getId();
    }

    public List<FavoriteResponse> findAllFavorites(String principal) {
        Member findMember = findMemberByPrincipal(principal);
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(findMember.getId());

        List<FavoriteResponse> responseList = new ArrayList<>();
        for (Favorite favorite : favorites) {
            FavoriteStationResponse source = new FavoriteStationResponse(favorite.getSource());
            FavoriteStationResponse target = new FavoriteStationResponse(favorite.getTarget());
            responseList.add(new FavoriteResponse(favorite.getId(), source, target));
        }

        return responseList;
    }

    private Member findMemberByPrincipal(String principal) {
        return memberRepository.findByEmail(principal)
                .orElseThrow(NoSuchElementException::new);
    }
}
