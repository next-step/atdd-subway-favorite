package nextstep.favorites.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.common.exception.BusinessException;
import nextstep.common.exception.ErrorResponse;
import nextstep.common.exception.LoginException;
import nextstep.favorites.application.dto.FavoriteRequest;
import nextstep.favorites.application.dto.FavoriteResponse;
import nextstep.favorites.domain.Favorite;
import nextstep.favorites.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final StationRepository stationRepository;

    private final MemberRepository memberRepository;


    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository, MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long addFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        Member member = getMemberById(memberId);
        Station targetStation = getStationById(favoriteRequest.getTarget());
        Station sourceStation = getStationById(favoriteRequest.getSource());
        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        favoriteRepository.save(favorite);

        member.addFavorite(favorite);
        return favorite.getId();
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new LoginException(ErrorResponse.INVALID_TOKEN_VALUE));
    }
    private Station getStationById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new BusinessException(ErrorResponse.NOT_FOUND_EMAIL));
    }

    public List<FavoriteResponse> getFavorites(Long memberId) {
        Member member = getMemberById(memberId);
        List<Favorite> favorites = member.getFavorites();
        return favorites.stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public void remove(Long memberId, Long favoriteId) {
        Member member = getMemberById(memberId);

        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(() -> new BusinessException(ErrorResponse.NOT_FOUND_FAVORITE));

        if (!member.getFavorites().contains(favorite)) {
            throw new BusinessException(ErrorResponse.FORBIDDEN);
        }
        favorite.delete();
        favoriteRepository.deleteById(favoriteId);
    }
}
