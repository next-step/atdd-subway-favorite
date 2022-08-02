package nextstep.favorite.application;

import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           StationService stationService,
                           MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public Long createFavorite(String email, Long source, Long target) {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(NoSuchElementException::new);

        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);

        Favorite favorite = favoriteRepository.save(new Favorite(findMember.getId(), sourceStation, targetStation));
        return favorite.getId();
    }
}
