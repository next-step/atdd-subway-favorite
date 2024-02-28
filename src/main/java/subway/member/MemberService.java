package subway.member;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dto.member.MemberRequest;
import subway.dto.member.MemberResponse;

@Transactional(readOnly = true)
@Service
public class MemberService {
	private final MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	private Member findById(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(EntityNotFoundException::new);
	}

	private Member findByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("해당하는 email이 없습니다."));
	}

	@Transactional
	public Member findByEmailIfNotExistsRegister(MemberRequest request) {
		return memberRepository.findByEmail(request.getEmail())
			.orElseGet(() -> memberRepository.save(request.toMember()));
	}

	@Transactional
	public MemberResponse createMember(MemberRequest request) {
		Member member = memberRepository.save(request.toMember());
		return MemberResponse.of(member);
	}

	public MemberResponse findMember(Long id) {
		return MemberResponse.of(findById(id));
	}

	@Transactional
	public void updateMember(Long id, MemberRequest param) {
		Member member = findById(id);
		member.update(param.toMember());
	}

	@Transactional
	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}

	public Member findMemberByEmail(String email) {
		return findByEmail(email);
	}

	public MemberResponse findMe(LoginMember loginMember) {
		Member member = findByEmail(loginMember.getEmail());
		return MemberResponse.of(member);
	}
}
