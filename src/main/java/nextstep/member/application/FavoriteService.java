package nextstep.member.application;

import nextstep.auth.principal.UserPrincipal;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository
            , StationRepository stationRepository, LineRepository lineRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public FavoriteResponse createFavorites(FavoriteRequest request, UserPrincipal userPrincipal) {
        Member member = memberRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new DataIntegrityViolationException("사용자가 존재하지 않습니다."));
        Station source = stationRepository.findById(request.getSource())
                .orElseThrow(() -> new DataIntegrityViolationException("역이 존재하지 않습니다."));
        Station target = stationRepository.findById(request.getTarget())
                .orElseThrow(() -> new DataIntegrityViolationException("역이 존재하지 않습니다."));

        List<Line> lines = lineRepository.findAll();
        for (Line line : lines) {
            if (!line.containsSection(source) || !line.containsSection(target)) {
                throw new DataIntegrityViolationException("");
            }
        }

        Favorite favorite = favoriteRepository.save(new Favorite(source, target, member.getId()));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> selectFavorites(UserPrincipal userPrincipal) {
        Member member = memberRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException());
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());

        return FavoriteResponse.of(favorites);
    }

    @Transactional
    public void deleteFavorites(Long id, UserPrincipal userPrincipal) {
        Member member = memberRepository.findByEmail(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException());
        favoriteRepository.deleteByIdAndMemberId(id, member.getId());
    }
}
