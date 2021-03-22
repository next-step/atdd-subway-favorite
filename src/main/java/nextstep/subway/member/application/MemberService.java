package nextstep.subway.member.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final StationService stationService;

    public MemberService(MemberRepository memberRepository, StationService stationService) {
        this.memberRepository = memberRepository;
        this.stationService = stationService;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = findByIdOrThrow(id);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = findByIdOrThrow(id);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public List<FavoriteResponse> findAllFavoriteOfMine(long id) {
        return findByIdOrThrow(id)
                    .getFavorites()
                    .stream()
                    .map(FavoriteResponse::of)
                    .collect(Collectors.toList());
    }

    public Favorite addFavorite(long id, FavoriteRequest favoriteRequest) {
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());

        return findByIdOrThrow(id).addFavorite(source, target);
    }

    public void removeFavorite(long id, long favoriteId) {
        findByIdOrThrow(id).removeFavorite(favoriteId);
    }

    private Member findByIdOrThrow(long id) {
        return memberRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}