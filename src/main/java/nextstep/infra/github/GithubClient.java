package nextstep.infra.github;

import nextstep.infra.github.dto.GithubAccessTokenRequest;
import nextstep.infra.github.dto.GithubAccessTokenResponse;
import nextstep.infra.github.dto.GithubProfileResponse;
import org.springframework.http.HttpHeaders;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GithubClient {
    @POST
    Call<GithubAccessTokenResponse> callAccessTokenApi(@Url String url, @Body GithubAccessTokenRequest githubAccessTokenRequest);

    @GET
    Call<GithubProfileResponse> callGithubProfileApi(@Url String url, @Header(HttpHeaders.AUTHORIZATION) String token);
}
