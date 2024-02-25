package nextstep.favorite.application;

import nextstep.favorite.application.dto.FavoriteCreateCommand;
import nextstep.favorite.application.dto.FavoriteDeleteCommand;
import nextstep.favorite.application.dto.FavoriteDto;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.favorite.domain.exception.FavoriteConflictException;
import nextstep.favorite.domain.exception.FavoriteNotFoundException;
import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.domain.exception.MemberNotFoundException;
import nextstep.path.domain.PathFinder;
import nextstep.path.domain.exception.PathNotFoundException;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import nextstep.station.domain.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private MemberRepository memberRepository;
    private PathFinder pathFinder;

    public FavoriteService(
            FavoriteRepository favoriteRepository,
            LineRepository lineRepository,
            StationRepository stationRepository,
            MemberRepository memberRepository,
            PathFinder pathFinder
    ) {
        this.favoriteRepository = favoriteRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
        this.pathFinder = pathFinder;
    }

    @Transactional
    public void createFavorite(FavoriteCreateCommand command) {
        Station source = stationRepository.findById(command.getSource()).orElseThrow(StationNotFoundException::new);
        Station target = stationRepository.findById(command.getTarget()).orElseThrow(StationNotFoundException::new);
        Member member = memberRepository.findById(command.getMember()).orElseThrow(MemberNotFoundException::new);

        validateDataForCreatingFavorite(member, source, target);

        Favorite favorite = Favorite.create(source, target, member);
        favoriteRepository.save(favorite);
    }

    public List<FavoriteDto> findFavorites() {
        List<Favorite> favorites = favoriteRepository.findAll();
        return favorites.stream().map(FavoriteDto::from).collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(FavoriteDeleteCommand command) {
        Favorite favorite = favoriteRepository.findById(command.getFavorite())
                .orElseThrow(FavoriteNotFoundException::new);
        favorite.validateCommandingMember(command.getMember());
        favoriteRepository.deleteById(favorite.getId());
    }

    private void validateDataForCreatingFavorite(Member member, Station source, Station target) {
        if (favoriteRepository.existsByMemberIdAndSourceStationIdAndTargetStationId(
                member.getId(), source.getId(), target.getId()
        )) { throw new FavoriteConflictException(); }

        List<Line> lines = lineRepository.findAll();
        if (!pathFinder.isConnected(lines, source, target)) {
            throw new PathNotFoundException();
        };
    }
}
