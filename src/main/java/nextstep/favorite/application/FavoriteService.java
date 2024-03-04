package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.path.PathService;
import nextstep.station.Station;
import nextstep.station.StationRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;
    private final PathService pathService;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            StationRepository stationRepository,
            MemberRepository memberRepository,
            PathService pathService
    ) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
        this.pathService = pathService;
    }

    public Long createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberRepository.findByEmail(loginMember.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("해당 이메일 계정을 찾을 수 없습니다."));
        Station sourceStation = stationRepository.findById(request.getSource())
                .orElseThrow(() -> new EntityNotFoundException("해당 역이 존재하지 않습니다."));
        Station targetStation = stationRepository.findById(request.getTarget())
                .orElseThrow(() -> new EntityNotFoundException("해당 역이 존재하지 않습니다."));

        pathService.findShortestPath(sourceStation.getId(), targetStation.getId());

        return favoriteRepository.save(new Favorite(member.getId(), sourceStation.getId(), targetStation.getId())).getId();
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberRepository.findByEmail(loginMember.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("해당 이메일 계정을 찾을 수 없습니다."));
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());
        return favorites.stream()
                .map(favorite -> new FavoriteResponse(
                        favorite.getId(),
                        stationRepository.findById(favorite.getSourceStationId())
                                .orElseThrow(() -> new EntityNotFoundException("해당 역이 존재하지 않습니다.")),
                        stationRepository.findById(favorite.getTargetStationId())
                                .orElseThrow(() -> new EntityNotFoundException("해당 역이 존재하지 않습니다."))
                ))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long id) {
        Member member = memberRepository.findByEmail(loginMember.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("해당 이메일 계정을 찾을 수 없습니다."));
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 즐겨찾기를 찾을 수 없습니다."));
        favorite.validate(member.getId());

        favoriteRepository.deleteById(id);
    }
}
