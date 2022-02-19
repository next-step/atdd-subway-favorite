package nextstep.member.application;

import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public List<FavoriteResponse> findFavorite(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return findFavoriteResponses(member.getFavorites());
    }

    private List<FavoriteResponse> findFavoriteResponses(List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public FavoriteResponse addFavorite(Long id, FavoriteRequest request) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        Station source = stationService.findById(request.getSource());
        Station target = stationService.findById(request.getTarget());
        Favorite favorite = new Favorite(member, source, target);

        favoriteRepository.save(favorite);
        member.addFavorite(favorite);

        return FavoriteResponse.of(favorite);
    }

    public void deleteFavorite(Long id, Long favoriteId) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(RuntimeException::new);

        member.removeFavorite(favorite);
    }
}