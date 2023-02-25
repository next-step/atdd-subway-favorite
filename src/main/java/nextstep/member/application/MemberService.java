package nextstep.member.application;

import nextstep.auth.AuthMember;
import nextstep.error.exception.BusinessException;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.StationService;
import org.springframework.stereotype.Service;

import static nextstep.error.exception.ErrorCode.MEMBER_NOT_EXISTS;
import static nextstep.error.exception.ErrorCode.MISMATCHED_PASSWORD;

@Service
public class MemberService {
	private final MemberRepository memberRepository;
	private final FavoriteRepository favoriteRepository;
	private final StationService stationService;

	public MemberService(MemberRepository memberRepository, FavoriteRepository favoriteRepository, StationService stationService) {
		this.memberRepository = memberRepository;
		this.favoriteRepository = favoriteRepository;
		this.stationService = stationService;
	}

	public MemberResponse createMember(MemberRequest request) {
		Member member = memberRepository.save(request.toMember());
		return MemberResponse.of(member);
	}

	public MemberResponse findMember(Long id) {
		Member member = memberRepository.findById(id).orElseThrow(() -> new BusinessException(MEMBER_NOT_EXISTS));
		return MemberResponse.of(member);
	}

	public void updateMember(Long id, MemberRequest param) {
		Member member = memberRepository.findById(id).orElseThrow(() -> new BusinessException(MEMBER_NOT_EXISTS));
		member.update(param.toMember());
	}

	public void deleteMember(Long id) {
		memberRepository.findById(id).orElseThrow(() -> new BusinessException(MEMBER_NOT_EXISTS));
		memberRepository.deleteById(id);
	}

	public Member findMemberByEmailAndPassword(String email, String password) {
		return memberRepository.findByEmail(email)
				.filter(it -> it.checkPassword(password))
				.orElseThrow(() -> new BusinessException(MISMATCHED_PASSWORD));
	}

	public MemberResponse findMemberByEmail(String email) {
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new BusinessException(MEMBER_NOT_EXISTS));
		return MemberResponse.of(member);
	}

	public MemberResponse findMemberByGithubEmailOrElseCreateMember(String id, String email) {
		Member member = memberRepository.findByEmail(email)
				.orElse(memberRepository.save(new Member(email, id, null)));
		return MemberResponse.of(member);
	}

	public AuthMember findAuthMemberByEmail(String email) {
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new BusinessException(MEMBER_NOT_EXISTS));
		return AuthMember.of(member);
	}

    public FavoriteResponse createFavorite(AuthMember authMember, FavoriteRequest favoriteRequest) {
		Member member = memberRepository.findById(authMember.getId()).orElseThrow(() -> new BusinessException(MEMBER_NOT_EXISTS));
		Station source = stationService.findById(favoriteRequest.getSource());
		Station target = stationService.findById(favoriteRequest.getTarget());
		Favorite favorite = new Favorite(member.getId(), source, target);
		favoriteRepository.save(favorite);

		return FavoriteResponse.of(favorite);
    }
}