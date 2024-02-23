package nextstep.favorite.application;

import nextstep.exception.ApplicationException;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static nextstep.exception.ExceptionMessage.NO_EXISTS_MEMBER_EXCEPTION;
import static nextstep.exception.ExceptionMessage.NO_EXISTS_STATION_EXCEPTION;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(FavoriteRequest request, LoginMember member) {
        Favorite favorite = new Favorite(getStation(request.getSource()), getStation(request.getTarget()), getMember(member));
        favoriteRepository.save(favorite);
        return FavoriteResponse.from(favorite);
    }

    /**
     * TODO: StationResponse 를 응답하는 FavoriteResponse 로 변환해야 합니다.
     *
     * @return
     */
    public List<FavoriteResponse> findFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        return null;
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }

    private Member getMember(LoginMember loginMember) {
        return memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(() -> new ApplicationException(NO_EXISTS_MEMBER_EXCEPTION.getMessage()));
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new ApplicationException(NO_EXISTS_STATION_EXCEPTION.getMessage()));
    }
}
