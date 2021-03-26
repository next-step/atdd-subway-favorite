package nextstep.subway.member.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.auth.ui.LoginMemberPort;
import nextstep.subway.member.domain.CustomUserDetails;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements LoginMemberPort {
    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails getLoginMember(String principal) {
        Member member = memberRepository.findByEmail(principal).orElseThrow(RuntimeException::new);
        return new CustomUserDetails(member.getId(), member.getEmail(), member.getPassword());
    }

    @Override
    public UserDetails getUserDetailFromPayload(String payload) {
        try {
            return new ObjectMapper().readValue(payload, CustomUserDetails.class);
        }catch (JsonProcessingException e){
            return null;
        }
    }
}
