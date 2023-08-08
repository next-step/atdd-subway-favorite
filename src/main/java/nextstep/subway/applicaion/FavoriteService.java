package nextstep.subway.applicaion;

import java.util.List;
import nextstep.auth.AuthenticationException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.domain.SubwayMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;
    private final LineRepository lineRepository;

    public FavoriteService(
        FavoriteRepository favoriteRepository,
        StationRepository stationRepository,
        MemberRepository memberRepository,
        LineRepository lineRepository
    ) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Long createFavorite(String email, FavoriteRequest favoriteRequest) {
        Member member = findMemberByEmail(email);
        Station source = findStationById(favoriteRequest.getSource());
        Station target = findStationById(favoriteRequest.getTarget());
        SubwayMap subwayMap = new SubwayMap(lineRepository.findAll());

        if (subwayMap.isSeparated(source, target)) {
            throw new IllegalArgumentException(
                String.format(
                    "즐겨찾기에 등록하기 위해선 연결된 역이어야 합니다. 입력된 출발역:%s, 도착역:%s",
                    source.getName(),
                    target.getName()
                ));
        }

        Favorite favorite = new Favorite(member.getId(), source, target);
        return favoriteRepository.save(favorite).getId();
    }

    public List<FavoriteResponse> findAll(String email) {
        Member member = findMemberByEmail(email);
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());

        return FavoriteResponse.listOf(favorites);
    }

    @Transactional
    public void delete(String email, Long id) {
        Member member = findMemberByEmail(email);
        Favorite favorite = findFavoriteById(id);

        if (isDifferentMember(member, favorite)) {
            throw new AuthenticationException(
                String.format(
                    "다른 사용자의 즐겨찾기는 삭제할 수 없습니다. 즐겨찾기 주인 Id:%s, 요청자Id:%s",
                    member.getId(),
                    favorite.getMemberId()
                ));
        }

        favoriteRepository.deleteById(id);
    }

    private boolean isDifferentMember(Member member, Favorite favorite) {
        return !favorite.getId().equals(member.getId());
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(IllegalArgumentException::new);
    }

    private Favorite findFavoriteById(Long id) {
        return favoriteRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);
    }
}
