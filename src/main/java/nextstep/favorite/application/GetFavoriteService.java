package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.domain.LoginMemberForFavorite;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetFavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public GetFavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public List<FavoriteResponse> getFavorites(LoginMemberForFavorite loginMember) {
        Member member = findMemberByEmail(loginMember.getEmail());
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());
        return mapToFavoriteResponses(favorites);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 멤버가 존재하지 않습니다. email: " + email));
    }

    private List<FavoriteResponse> mapToFavoriteResponses(List<Favorite> favorites) {
        List<Long> sourceStationIds = favorites.stream().map(Favorite::getSourceStationId).collect(Collectors.toList());
        List<Long> targetStationIds = favorites.stream().map(Favorite::getTargetStationId).collect(Collectors.toList());

        List<Station> sourceStations = stationRepository.findAllByIdIn(sourceStationIds);
        List<Station> targetStations = stationRepository.findAllByIdIn(targetStationIds);

        return favorites.stream()
                .map(favorite -> mapToFavoriteResponse(favorite, sourceStations, targetStations))
                .collect(Collectors.toList());
    }

    private static FavoriteResponse mapToFavoriteResponse(Favorite favorite, List<Station> sourceStations, List<Station> targetStations) {
        Station sourceStation = sourceStations.stream()
                .filter(station -> station.getId().equals(favorite.getSourceStationId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("출발역이 존재하지 않습니다. stationId: " + favorite.getSourceStationId()));

        Station targetStation = targetStations.stream()
                .filter(station -> station.getId().equals(favorite.getTargetStationId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("도착역이 존재하지 않습니다. stationId: " + favorite.getTargetStationId()));

        return FavoriteResponse.of(favorite, sourceStation, targetStation);
    }
}
