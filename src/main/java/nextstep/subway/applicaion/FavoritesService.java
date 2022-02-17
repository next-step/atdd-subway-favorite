package nextstep.subway.applicaion;

import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoritesRequest;
import nextstep.subway.applicaion.dto.FavoritesResponse;
import nextstep.subway.domain.Favorites;
import nextstep.subway.domain.FavoritesRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.ui.exception.FavoritesException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class FavoritesService {
    private final FavoritesRepository favoritesRepository;
    private final StationService stationService;
    private final MemberRepository memberRepository;

    public FavoritesService(FavoritesRepository favoritesRepository, StationService stationService, MemberRepository memberRepository) {
        this.favoritesRepository = favoritesRepository;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public Long saveFavorites(FavoritesRequest favoritesRequest, LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(RuntimeException::new);
        Station source = stationService.findById(favoritesRequest.getSource());
        Station target = stationService.findById(favoritesRequest.getTarget());

        Favorites favorites = new Favorites(member, source, target);
        return favoritesRepository.save(favorites).getId();
    }

    @Transactional(readOnly = true)
    public List<FavoritesResponse> findByMemberId(LoginMember loginMember) {
        List<Favorites> favoriteses = favoritesRepository.findByMemberId(loginMember.getId());
        return FavoritesResponse.convertFavoritesResponses(favoriteses);
    }

    public void deleteFavorites(LoginMember loginMember, Long id) {
        Favorites findFavorites = favoritesRepository.findById(id)
                .orElseThrow(() -> new FavoritesException("존재하지 않는 즐겨찾기 입니다."));
        if (findFavorites.canDeleteFavorites(loginMember.getId())) {
            favoritesRepository.deleteById(id);
        }
    }
}
