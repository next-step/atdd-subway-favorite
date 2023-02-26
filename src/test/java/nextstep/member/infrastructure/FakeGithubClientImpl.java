package nextstep.member.infrastructure;


import java.util.Arrays;
import nextstep.member.infrastructure.dto.ProfileDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class FakeGithubClientImpl implements SocialClient {

    @Override
    public ProfileDto getProfileFromGithub(String code) {
        DummyGithubResponses profile = DummyGithubResponses.getEmailByCode(code);
        return ProfileDto.from(profile.email);
    }

    public enum DummyGithubResponses {
        사용자1("832ovnq039hfjn", "access_token_1", "email1@email.com"),
        사용자2("mkfo0aFa03m", "access_token_2", "email2@email.com"),
        사용자3("m-a3hnfnoew92", "access_token_3", "email3@email.com"),
        사용자4("nvci383mciq0oq", "access_token_4", "email4@email.com");

        private final String code;
        private final String accessToken;
        private final String email;

        DummyGithubResponses(String code, String accessToken, String email) {
            this.code = code;
            this.accessToken = accessToken;
            this.email = email;
        }

        public static DummyGithubResponses getEmailByCode(String code) {
            return Arrays.stream(values())
                .filter(v -> v.code.equals(code))
                .findFirst()
                .get();
        }

        public String getCode() {
            return code;
        }

        public String getEmail() {
            return email;
        }
    }
}
