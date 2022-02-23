package nextstep.member.application;

import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.*;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private MemberRepository memberRepository;
    private FavoriteRepository favoriteRepository;
    private StationService stationService;

    public MemberService(MemberRepository memberRepository,
                         FavoriteRepository favoriteRepository,
                         StationService stationService) {
        this.memberRepository = memberRepository;
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }

    public MemberResponse updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());

        return MemberResponse.of(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberResponse findMemberOfMine(LoginMember loginMember) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(RuntimeException::new);

        return MemberResponse.of(member);
    }

    public MemberResponse updateMemberOfMine(LoginMember loginMember, MemberRequest memberRequest) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(RuntimeException::new);
        member.update(memberRequest.toMember());

        return MemberResponse.of(member);
    }

    public void deleteMemberOfMine(LoginMember loginMember) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(RuntimeException::new);
        memberRepository.delete(member);
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(RuntimeException::new);
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        Favorite favorite = new Favorite(member, source, target);
        favoriteRepository.save(favorite);

        return FavoriteResponse.of(favorite);
    }
}