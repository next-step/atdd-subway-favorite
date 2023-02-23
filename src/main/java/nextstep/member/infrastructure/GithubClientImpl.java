package nextstep.member.infrastructure;

import nextstep.member.infrastructure.dto.GithubTokenRequest;
import nextstep.member.infrastructure.dto.GithubTokenResponse;
import nextstep.member.infrastructure.dto.GithubProfileResponse;
import nextstep.member.infrastructure.dto.ProfileDto;
import nextstep.member.infrastructure.exception.InternalServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Profile("!test")
public class GithubClientImpl implements SocialClient {

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.url.access-token}")
    private String tokenUrl;
    @Value("${github.url.profile}")
    private String profileUrl;

    @Override
    public ProfileDto getProfileFromGithub(String code) {
        String accessToken = getAccessToken(code);
        GithubProfileResponse githubProfileResponse = getGithubProfileFromGithub(accessToken);
        return ProfileDto.from(githubProfileResponse.getEmail());
    }

    private String getAccessToken(String code) {
        GithubTokenRequest githubTokenRequest = new GithubTokenRequest(
            code,
            clientId,
            clientSecret
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity httpEntity = new HttpEntity(githubTokenRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        String accessToken = restTemplate
            .exchange(tokenUrl, HttpMethod.POST, httpEntity, GithubTokenResponse.class)
            .getBody()
            .getAccessToken();

        if (accessToken == null) {
            throw new InternalServerException();
        }

        return accessToken;
    }

    private GithubProfileResponse getGithubProfileFromGithub(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + accessToken);

        HttpEntity httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            return restTemplate
                .exchange(profileUrl, HttpMethod.GET, httpEntity, GithubProfileResponse.class)
                .getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException();
        }
    }
}
