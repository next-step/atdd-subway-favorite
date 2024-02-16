package nextstep.auth;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonProperty;
=======
>>>>>>> 6d60211d ([test] 깃허브 로그인 인수테스트 작성)
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubAccessTokenRequest {
    private String code;
<<<<<<< HEAD
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
=======
    private String clientId;
>>>>>>> 6d60211d ([test] 깃허브 로그인 인수테스트 작성)
    private String clientSecret;
}
