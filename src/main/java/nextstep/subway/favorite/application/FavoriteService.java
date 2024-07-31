package nextstep.subway.favorite.application;

import nextstep.subway.dto.PathRequest;
import nextstep.subway.entity.Station;
import nextstep.subway.exception.IllegalFavoriteException;
import nextstep.subway.exception.NoSuchStationException;
import nextstep.subway.favorite.application.dto.FavoriteRequest;
import nextstep.subway.favorite.application.dto.FavoriteResponse;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.repository.StationRepository;
import nextstep.subway.service.PathService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;
    private PathService pathService;
    private MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository
            , StationRepository stationRepository
            , PathService pathService
            , MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.pathService = pathService;
        this.memberService = memberService;
    }

    @Transactional
    public Long createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());

        Station sourceStation = getStation(request.getSource());
        Station targetStation = getStation(request.getTarget());

        checkPathConnectionOrThrow(request);

        Favorite favorite = new Favorite(member, sourceStation, targetStation);

        favoriteRepository.save(favorite);

        return favorite.getId();
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());
        List<Favorite> favorites = favoriteRepository.findByMember(member);

        return favorites.stream()
                .map(f -> FavoriteResponse.from(f.getId(), f.getSourceStation(), f.getTargetStation()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(LoginMember loginMember, Long id) {
        Member member = memberService.findMemberByEmail(loginMember.getEmail());

        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new IllegalFavoriteException("존재하지 않는 즐겨찾기입니다."));

        if (!favorite.isSameMember(member)) {
            throw new IllegalFavoriteException("즐겨찾기를 삭제할 수 없습니다.");
        }

        favoriteRepository.deleteById(id);
    }

    private void checkPathConnectionOrThrow(FavoriteRequest request) {
        pathService.getPath(new PathRequest(request.getSource(), request.getTarget()));
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NoSuchStationException("존재하지 않는 역입니다."));
    }
}
