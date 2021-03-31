package nextstep.subway.member.application;
import nextstep.subway.member.domain.*;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.member.dto.FavoriteResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {
    private MemberRepository memberRepository;
    private StationService stationService;
    private FavoriteRepository favoriteRepository;

    public MemberService(MemberRepository memberRepository, StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(NoSuchElementException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public void updateMine(LoginMember loginMember, MemberRequest param) {
        updateMember(loginMember.getId(), param);
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(NoSuchElementException::new);
        Station upStation = stationService.findById(favoriteRequest.getSource());
        Station downStation = stationService.findById(favoriteRequest.getTarget());
        favoriteRepository.save(new Favorite(member.getId(), upStation, downStation));
        return new FavoriteResponse(member.getId(), StationResponse.of(upStation), StationResponse.of(downStation));
    }


    public List<FavoriteResponse> searchFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
        return favorites.stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    public void deleteFavorites(Long id) {
        favoriteRepository.deleteById(id);
    }
}