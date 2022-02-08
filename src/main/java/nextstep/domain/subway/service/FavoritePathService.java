package nextstep.domain.subway.service;

import nextstep.auth.authentication.LoginMember;
import nextstep.domain.member.domain.Member;
import nextstep.domain.member.domain.MemberRepository;
import nextstep.domain.subway.domain.FavoritePath;
import nextstep.domain.subway.domain.FavoritePathRepository;
import nextstep.domain.subway.domain.Station;
import nextstep.domain.subway.domain.StationRepository;
import nextstep.domain.subway.dto.FavoritePathRequest;
import org.springframework.stereotype.Service;

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

    public long createFavorite(LoginMember loginMember, FavoritePathRequest favoritePathRequest) {
        Station startStation = stationRepository.findOneById(favoritePathRequest.getStartStationId());
        Station endStation = stationRepository.findOneById(favoritePathRequest.getEndStationId());
        Member member = memberRepository.findOneById(loginMember.getId());

        FavoritePath favoritePath = new FavoritePath(startStation, endStation, member);
        favoritePath.validCheck();
        favoritePathRepository()

    }
}
