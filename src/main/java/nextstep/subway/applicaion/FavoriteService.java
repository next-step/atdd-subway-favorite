package nextstep.subway.applicaion;

import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.applicaion.exception.BusinessException;
import nextstep.subway.applicaion.exception.NotFoundException;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMember(loginMember.getId());
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());

        Favorite favorite = new Favorite(member, source, target);
        return FavoriteResponse.of(favoriteRepository.save(favorite));
    }

    public List<FavoriteResponse> getFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
        List<FavoriteResponse> favoriteResponses = favorites.stream().map(FavoriteResponse::of).collect(Collectors.toList());
        return favoriteResponses;
    }

    public void deleteFavorite(Long favoriteId, LoginMember loginMember) {
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(NotFoundException::new);

        if (!favorite.isYours(loginMember)) {
            throw new BusinessException("당신의 즐겨찾기 아님", HttpStatus.FORBIDDEN);
        }

        favoriteRepository.delete(favorite);
    }
}
