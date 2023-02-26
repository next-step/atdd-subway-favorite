package nextstep.favorite.application;

import nextstep.exception.UnAuthorizedException;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.domain.Favorites;
import nextstep.favorite.ui.request.FavoriteRequest;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.member.infrastructure.dto.LoginMember;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
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
    public Favorite createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMemberById(loginMember.getId());
        FavoriteStations favoriteStations = findStations(favoriteRequest);
        Favorite favorite = new Favorite(member, favoriteStations.getSource(), favoriteStations.getTarget());
        member.addFavorite(favorite);

        return favoriteRepository.save(favorite);
    }

    public Favorites getFavorite(Long id) {
        Member member = memberService.findMemberById(id);
        return member.getFavorites();
    }

    @Transactional
    public void deleteFavorite(Long memberId, Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(RuntimeException::new);
        if (!Objects.equals(favorite.getMemberId(), memberId)) {
            throw new UnAuthorizedException();
        }
        favoriteRepository.deleteById(favoriteId);
    }

    private FavoriteStations findStations(FavoriteRequest favoriteRequest) {
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        return new FavoriteStations(source, target);
    }

    class FavoriteStations {

        private Station source;
        private Station target;

        public FavoriteStations(Station source, Station target) {
            this.source = source;
            this.target = target;
        }

        public Station getSource() {
            return source;
        }

        public Station getTarget() {
            return target;
        }
    }
}
