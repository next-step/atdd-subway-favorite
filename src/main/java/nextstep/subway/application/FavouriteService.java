package nextstep.subway.application;

import nextstep.subway.application.dto.FavouriteRequest;
import nextstep.subway.application.dto.FavouriteResponse;
import nextstep.subway.application.dto.station.StationResponse;
import nextstep.subway.domain.favourite.Favourite;
import nextstep.subway.domain.favourite.FavouriteRepository;
import nextstep.subway.domain.favourite.FavouriteValidator;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.member.Member;
import nextstep.subway.domain.member.MemberRepository;
import nextstep.subway.domain.path.PathValidator;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.utils.exception.FavouriteNotFoundException;
import nextstep.utils.exception.MemberException;
import nextstep.utils.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavouriteService {
    private FavouriteRepository favouriteRepository;
    private StationRepository stationRepository;
    private MemberRepository memberRepository;
    private LineRepository lineRepository;

    public FavouriteService(FavouriteRepository favouriteRepository, StationRepository stationRepository,
                            MemberRepository memberRepository, LineRepository lineRepository) {
        this.favouriteRepository = favouriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
        this.lineRepository = lineRepository;
    }

    public Long add(Long memberId, FavouriteRequest favouriteRequest) {
        Member authenticatedMember = findMemberById(memberId);

        Station upStation = findStationById(favouriteRequest.getSource());
        Station downStation = findStationById(favouriteRequest.getTarget());

        PathValidator.validateNotLinked(lineRepository.findAll(), upStation, downStation);

        Favourite newFavourite = createFavourite(authenticatedMember, upStation, downStation);
        favouriteRepository.save(newFavourite);

        return newFavourite.getId();
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(StationNotFoundException::new);
    }

    private Favourite createFavourite(Member authenticatedMember, Station upStation, Station downStation) {
        return Favourite.of(authenticatedMember, upStation, downStation);
    }

    @Transactional(readOnly = true)
    public List<FavouriteResponse> findAll(Long id) {
        Member authenticatedMember = findMemberById(id);

        List<Favourite> favouriteList = favouriteRepository.findAllByMember(authenticatedMember);

        return favouriteList.stream()
                .map(favourite -> FavouriteResponse.of(
                        favourite,
                        StationResponse.of(favourite.getUpStation()),
                        StationResponse.of(favourite.getDownStation())
                ))
                .collect(Collectors.toList());
    }

    public void delete(Long memberId, Long favouriteId) {
        Member authenticatedMember = findMemberById(memberId);
        Favourite favourite = findFavouriteById(favouriteId);

        FavouriteValidator.validateAcceptable(favourite, authenticatedMember);

        favouriteRepository.delete(favourite);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberException::new);
    }

    private Favourite findFavouriteById(Long favouriteId) {
        return favouriteRepository.findById(favouriteId)
                .orElseThrow(FavouriteNotFoundException::new);
    }
}
