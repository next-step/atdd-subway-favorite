package nextstep.favorite.application;

import static nextstep.global.exception.ExceptionCode.NOT_FOUND_MEMBER;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.Favorite.Builder;
import nextstep.favorite.infrastructrue.FavoriteRepository;
import nextstep.global.exception.CustomException;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.station.domain.Station;
import nextstep.station.infrastructure.StationRepository;
import nextstep.station.presentation.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * TODO: LoginMember 를 추가로 받아서 FavoriteRequest 내용과 함께 Favorite 를 생성합니다.
     *
     * @param request
     */
    public Long createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = findMemberByEmail(loginMember.getEmail());

        Favorite favorite = new Builder().memberId(member.getId())
                .sourceStationId(request.getSource())
                .targetStationId(request.getTarget())
                .build();

        Favorite newFavorite = favoriteRepository.save(favorite);
        return newFavorite.getId();
    }

    /**
     * TODO: StationResponse 를 응답하는 FavoriteResponse 로 변환해야 합니다.
     *
     * @return
     */
    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = findMemberByEmail(loginMember.getEmail());

        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
        List<Long> stationIds = new ArrayList<>();
        for (Favorite favorite : favorites) {
            stationIds.add(favorite.getSourceStationId());
            stationIds.add(favorite.getTargetStationId());
        }
        Map<Long, Station> stations = findByStationIds(stationIds);

        return favorites.stream().map(favorite -> {
            StationResponse source = StationResponse.fromEntity(stations.get(favorite.getSourceStationId()));
            StationResponse target = StationResponse.fromEntity(stations.get(favorite.getTargetStationId()));
            return new FavoriteResponse.Builder()
                    .id(favorite.getId())
                    .source(source)
                    .target(target)
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * TODO: 요구사항 설명에 맞게 수정합니다.
     * @param id
     */
    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));
    }

    private Map<Long, Station> findByStationIds(List<Long> stationIds) {
        List<Station> stations = this.stationRepository.findStationsByIdIn(stationIds);
        return stations.stream().collect(Collectors.toMap(Station::getId, station-> station));
    }
}
