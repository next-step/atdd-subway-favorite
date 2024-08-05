package nextstep.member.application.infrastructure;

import lombok.extern.slf4j.Slf4j;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.application.dto.TokenRequest;
import nextstep.member.application.dto.TokenResponse;
import nextstep.member.application.dto.github.GithubAccessTokenResponse;
import nextstep.member.domain.MemberClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class FavoriteMemberClient implements MemberClient {

    @Value("${member.enroll.url}")
    private String enrollUrl;

    @Value("${member.authorization.token.url}")
    private String tokenUrl;

    @Override
    public MemberResponse enrollMember(MemberRequest memberRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
                memberRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate
                .exchange(enrollUrl, HttpMethod.POST, httpEntity, MemberResponse.class)
                .getBody();
    }

    @Override
    public TokenResponse getMemberToken(TokenRequest tokenRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity(
                tokenRequest, headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate
                .exchange(tokenUrl, HttpMethod.POST, httpEntity, TokenResponse.class)
                .getBody();
    }
}
