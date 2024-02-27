package nextstep.subway.application.service;

import nextstep.member.application.service.MemberService;
import nextstep.member.domain.entity.Member;
import nextstep.subway.application.dto.FavoriteRequest;
import nextstep.subway.application.dto.FavoriteResponse;
import nextstep.subway.domain.entity.Favorite;
import nextstep.subway.domain.repository.FavoriteRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final PathService pathService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, PathService pathService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.pathService = pathService;
        this.memberService = memberService;
    }

    public FavoriteResponse saveFavorite(String email, FavoriteRequest request) {
        validFavoriteRequest(request);

        Favorite favorite = new Favorite(memberService.findMemberByEmail(email), request.getSource(), request.getTarget());
        return createFavoriteResponse(favoriteRepository.save(favorite));
    }

    public List<FavoriteResponse> findFavorites(String email) {
        Member member = memberService.findMemberByEmail(email);
        return favoriteRepository.findByMember(member).stream()
                .map(this::createFavoriteResponse)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(String email, Long id) {
        Member member = memberService.findMemberByEmail(email);

        favoriteRepository.findByIdAndMember(id, member).orElseThrow(() -> new EntityNotFoundException("즐겨찾기가 존재하지 않습니다."));

        favoriteRepository.deleteById(id);
    }

    private void validFavoriteRequest(FavoriteRequest request) {
        stationService.findStationById(request.getSource());
        stationService.findStationById(request.getTarget());

        pathService.getPath(request.getSource(), request.getTarget());
    }

    private FavoriteResponse createFavoriteResponse(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId()
                , stationService.findStationById(favorite.getSource())
                , stationService.findStationById(favorite.getTarget())
        );
    }
}
