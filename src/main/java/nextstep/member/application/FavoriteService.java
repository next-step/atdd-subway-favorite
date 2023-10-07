package nextstep.member.application;

import nextstep.member.application.dto.CreateFavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private StationService stationService;
    private MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public Favorite createFavorite(CreateFavoriteRequest request, String email) {
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return favoriteRepository.save(new Favorite(member, source, target));
    }

    public FavoriteResponse findFavorite(Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(RuntimeException::new);
        return FavoriteResponse.of(favorite);
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
