package nextstep.member.application;

import nextstep.exception.AccessDeniedException;
import nextstep.exception.notfoundexception.FavoriteNotFoundException;
import nextstep.exception.notfoundexception.MemberNotFoundException;
import nextstep.exception.notfoundexception.StationNotFoundException;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private SectionRepository sectionRepository;
    private StationRepository stationRepository;
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;

    public FavoriteService(StationRepository stationRepository,
                           FavoriteRepository favoriteRepository,
                           MemberRepository memberRepository,
                           SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public FavoriteResponse create(FavoriteRequest request, String username) {
        Station source = findSectionById(request.getSource());
        Station target = findSectionById(request.getTarget());
        Member member = findMemberByUsername(username);
        Favorite favorite = favoriteRepository.save(Favorite.of(member.getId(), source, target, sectionRepository.findAll()));
        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> findFavorites(String username) {
        Member member = findMemberByUsername(username);
        return favoriteRepository.findByMemberId(member.getId()).stream()
                .map(FavoriteResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long favoriteId, String username) {
        Favorite favorite = findFavoriteId(favoriteId);
        Member member = findMemberByUsername(username);
        if (!favorite.hasMemberId(member.getId())) {
            throw new AccessDeniedException();
        }
        favoriteRepository.delete(favorite);
    }

    private Favorite findFavoriteId(Long id) {
        return favoriteRepository.findById(id).orElseThrow(FavoriteNotFoundException::new);
    }

    private Member findMemberByUsername(String username) {
        return memberRepository.findByEmail(username).orElseThrow(MemberNotFoundException::new);
    }

    private Station findSectionById(Long id) {
        return stationRepository.findById(id).orElseThrow(StationNotFoundException::new);
    }
}
