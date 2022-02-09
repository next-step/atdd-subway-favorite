package nextstep.domain.subway.service;

import nextstep.auth.authentication.LoginMember;
import nextstep.domain.member.domain.Member;
import nextstep.domain.member.domain.MemberRepository;
import nextstep.domain.subway.domain.FavoritePath;
import nextstep.domain.subway.domain.FavoritePathRepository;
import nextstep.domain.subway.domain.Station;
import nextstep.domain.subway.domain.StationRepository;
import nextstep.domain.subway.dto.FavoritePathRequest;
import nextstep.domain.subway.dto.PathResponse;
import nextstep.domain.subway.dto.response.FavoritePathResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        FavoritePath favoritePath = new FavoritePath(startStation, endStation, member);
        favoritePath.validCheck();
        FavoritePath saveFavoritePath = favoritePathRepository.save(favoritePath);
        return saveFavoritePath.getId();
    }

    @Transactional(readOnly = true)
    public List<FavoritePathResponse> showFavorites(Member member) {
        Member findMember = memberRepository.findOneById(member.getId());
        return favoritePathRepository.findAllByMember(findMember)
                .stream().map(FavoritePathResponse::new).collect(toList());
    }

    @Transactional
    public void deleteFavorite(Long favoriteId) {
        FavoritePath favoritePath = favoritePathRepository.findOneById(favoriteId);
        favoritePath.validDelete();
    }
}
