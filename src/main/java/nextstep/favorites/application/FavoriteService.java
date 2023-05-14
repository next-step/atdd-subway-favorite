package nextstep.favorites.application;

import lombok.AllArgsConstructor;
import nextstep.exception.FavoriteNotFound;
import nextstep.exception.MemberNotFound;
import nextstep.exception.Unauthorized;
import nextstep.favorites.application.dto.FavoriteResponse;
import nextstep.favorites.domain.Favorite;
import nextstep.favorites.application.dto.FavoriteRequest;
import nextstep.favorites.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationService stationService;

    @Transactional
    public FavoriteResponse save(FavoriteRequest favoriteRequest, Long memberId) {
        Member member = getMemberOrException(memberId);
        List<Station> stations = stationService.findById(favoriteRequest.getSource(), favoriteRequest.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(stations.get(0), stations.get(1), member));
        return new FavoriteResponse(favorite.getId(), StationResponse.of(favorite.getSource()), StationResponse.of(favorite.getTarget()));
    }

    public List<FavoriteResponse> getFavorites(Long memberId) {
        List<FavoriteResponse> list = new ArrayList<>();
        Member member = getMemberOrException(memberId);
        List<Favorite> favoriteList = favoriteRepository.findAllByMember(member);
        for (Favorite favorite : favoriteList) {
            list.add(FavoriteResponse.of(favorite));
        }
        return list;
    }

    @Transactional
    public void delete(Long id, Long memberId) {
        getMemberOrException(memberId);
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(FavoriteNotFound::new);
        if(!memberId.equals(favorite.getMember().getId())) {
            throw new Unauthorized();
        }
        favoriteRepository.delete(favorite);
    }

    private Member getMemberOrException(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFound::new);
    }


}
