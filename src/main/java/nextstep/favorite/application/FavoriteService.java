package nextstep.favorite.application;

import io.jsonwebtoken.lang.Assert;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           StationService stationService,
                           MemberRepository memberRepository
    ) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public Long createFavorite(String principal, Long source, Long target) {
        Member findMember = findMemberByPrincipal(principal);

        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);

        Favorite favorite = favoriteRepository.save(new Favorite(findMember.getId(), sourceStation, targetStation));
        return favorite.getId();
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAllFavorites(String principal) {
        Member findMember = findMemberByPrincipal(principal);
        return favoriteRepository.findAllByMemberId(findMember.getId()).stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    private Member findMemberByPrincipal(String principal) {
        return memberRepository.findByEmail(principal)
                .orElseThrow(NoSuchElementException::new);
    }
}
