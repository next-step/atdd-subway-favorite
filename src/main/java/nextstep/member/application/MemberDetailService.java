package nextstep.member.application;

import nextstep.auth.application.UserDetail;
import nextstep.auth.application.UserDetailService;
import nextstep.exception.BadRequestException;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MemberDetailService implements UserDetailService {

    private final MemberRepository memberRepository;

    public MemberDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetail findUser(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("요청하신 이메일 정보는 정보는 올바르지 않은 정보입니다."));

        return UserDetail.builder()
                .id(member.getId())
                .age(member.getAge())
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }

    @Override
    public UserDetail findOrCreateUser(String email, Integer age) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();

            return UserDetail.builder()
                    .id(member.getId())
                    .age(member.getAge())
                    .email(member.getEmail())
                    .password(member.getPassword())
                    .build();
        }

        Member member = memberRepository.save(Member.builder()
                .email(email)
                .age(age)
                .password(UUID.randomUUID().toString())
                .build());

        return UserDetail.builder()
                .id(member.getId())
                .age(member.getAge())
                .email(member.getEmail())
                .password(UUID.randomUUID().toString())
                .build();
    }
}
