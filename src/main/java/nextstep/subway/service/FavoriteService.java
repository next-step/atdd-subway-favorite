package nextstep.subway.service;

import lombok.RequiredArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.MemberNotFountException;
import nextstep.subway.domain.PathFinder;
import nextstep.subway.domain.ShortestPathFinder;
import nextstep.subway.domain.entity.*;
import nextstep.subway.dto.FavoriteRequest;
import nextstep.subway.dto.FavoriteResponse;
import nextstep.subway.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteService {

    private final LineRepository lineRepository;
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    @Transactional
    public Long createFavorite(FavoriteRequest request, String username) {
        Station sourceStation = getStation(request.getSource());
        Station targetStation = getStation(request.getTarget());

        Member member = getMember(username);

        List<Line> lineList = lineRepository.findAll();
        PathFinder pathFinder = new ShortestPathFinder(lineList, sourceStation, targetStation);

        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        favoriteRepository.save(favorite);

        return favorite.getId();
    }

    public List<FavoriteResponse> getFavorites(String username) {
        Member member = getMember(username);

        List<Favorite> favoriteList = favoriteRepository.findAllByMember(member);
        return favoriteList.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(long id, String username) {
        Member member = getMember(username);
        favoriteRepository.deleteFavoriteByIdAndMember(id, member);
    }

    private Member getMember(String username) {
        return memberRepository.findByEmail(username)
                .orElseThrow(() -> new MemberNotFountException("member.0001"));
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException("section.not.found"));
    }
}
