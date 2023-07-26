package nextstep.member.application;

import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
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
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository
            , LineRepository lineRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public FavoriteResponse createFavorites(FavoriteRequest request, Member member) {
        Station source = stationRepository.findById(request.getSource())
                .orElseThrow(() -> new DataIntegrityViolationException("역이 존재하지 않습니다."));
        Station target = stationRepository.findById(request.getTarget())
                .orElseThrow(() -> new DataIntegrityViolationException("역이 존재하지 않습니다."));
        validatePath(source, target);

        Favorite favorite = favoriteRepository.save(new Favorite(source, target, member.getId()));
        return FavoriteResponse.of(favorite);
    }

    private void validatePath(Station source, Station target) {
        List<Line> lines = lineRepository.findAll();
        boolean containsPath = lines.stream()
                .anyMatch(line -> line.containsPath(source, target));

        if (!containsPath) {
            throw new DataIntegrityViolationException("연결되어 있지 않은 경로입니다.");
        }
    }

    public List<FavoriteResponse> selectFavorites(Member member) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());
        return FavoriteResponse.of(favorites);
    }

    @Transactional
    public void deleteFavorites(Long id, Member member) {
        favoriteRepository.deleteByIdAndMemberId(id, member.getId());
    }
}
