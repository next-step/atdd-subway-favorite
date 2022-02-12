package nextstep.domain.subway.service;

import nextstep.auth.authentication.LoginMember;
import nextstep.domain.member.domain.Member;
import nextstep.domain.member.domain.MemberRepository;
import nextstep.domain.subway.domain.FavoritePath;
import nextstep.domain.subway.domain.FavoritePathRepository;
import nextstep.domain.subway.domain.Station;
import nextstep.domain.subway.domain.StationRepository;
import nextstep.domain.subway.dto.FavoritePathRequest;
import nextstep.domain.subway.dto.response.FavoritePathResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
public class FavoritePathService {
    private final FavoritePathRepository favoritePathRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;

    public FavoritePathService(FavoritePathRepository favoritePathRepository, StationRepository stationRepository, MemberRepository memberRepository) {
        this.favoritePathRepository = favoritePathRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public long createFavorite(LoginMember loginMember, FavoritePathRequest favoritePathRequest) {
        Station startStation = stationRepository.findOneById(favoritePathRequest.getStartStationId());
        Station endStation = stationRepository.findOneById(favoritePathRequest.getEndStationId());
        Member member = memberRepository.findOneById(loginMember.getId());
        FavoritePath favoritePath = new FavoritePath(startStation, endStation, member.getId());
        FavoritePath saveFavoritePath = favoritePathRepository.save(favoritePath);
        return saveFavoritePath.getId();
    }

    @Transactional(readOnly = true)
    public List<FavoritePathResponse> showFavorites(Member member) {
        return favoritePathRepository.findAllByMemberId(member.getId())
                .stream().map(FavoritePathResponse::new).collect(toList());
    }

    @Transactional
    public void deleteFavorite(Long favoriteId, Long memberId) {
        //Todo : https://github.com/next-step/atdd-subway-favorite/pull/168#discussion_r803686310
        FavoritePath favoritePath = favoritePathRepository.findByIdAndMemberId(favoriteId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 수 없는 내용 입니다."));
        favoritePathRepository.delete(favoritePath);
    }
}
