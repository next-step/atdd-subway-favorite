package nextstep.favorite.application;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.exception.FavoriteException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberDetails;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(MemberDetails memberDetails, FavoriteRequest favoriteRequest) {
        Member member = getMember(memberDetails);
        Station source = getStation(favoriteRequest.getSource());
        Station target = getStation(favoriteRequest.getTarget());

        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));

        return new FavoriteResponse(favorite);

    }

    public FavoriteResponse getFavorite(MemberDetails memberDetails, FavoriteRequest favoriteRequest) {
        Member member = getMember(memberDetails);
        Favorite favorite = getFavorite(member.getId(), favoriteRequest.getSource(), favoriteRequest.getTarget());

        return new FavoriteResponse(favorite.getId());
    }


    public List<FavoriteResponse> getFavorites(MemberDetails memberDetails) {
        Member member = getMember(memberDetails);
        List<Favorite> favorites = getFavorites(member.getId());
        return favorites.stream()
                .map(FavoriteResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(MemberDetails memberDetails, Long id) {
        Member member = getMember(memberDetails);
        Favorite favorite = favoriteRepository.getById(id);
        checkRegistUser(member.getId(), favorite);
        favoriteRepository.deleteByIdAndMemberId(id, member.getId());
    }

    private Favorite getFavorite(Long memberId, Long sourceId, Long targetId) {
        return favoriteRepository.findByMemberIdAndSourceIdAndTargetId(memberId, sourceId, targetId)
                .orElseThrow(() -> new FavoriteException("CAN_NOT_FIND_FAVORITE"));
    }

    private List<Favorite> getFavorites(Long memberId) {
        return favoriteRepository.findByMemberId(memberId)
                .orElseThrow(() -> new FavoriteException("CAN_NOT_FIND_FAVORITES_INFORMATION"));
    }

    private Member getMember(MemberDetails memberDetails) {
        return memberRepository.findByEmail(memberDetails.getPrincipal())
                .orElseThrow(() -> new FavoriteException("CAN_NOT_FIND_MEMBER"));
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new FavoriteException("CAN_NOT_FIND_STATION"));
    }

    private void checkRegistUser(Long memberId, Favorite favorite) {
        if (!favorite.getMember().getId().equals(memberId)) {
            throw new AuthenticationException();
        }
    }
}
