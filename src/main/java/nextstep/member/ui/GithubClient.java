package nextstep.member.ui;

import nextstep.member.application.dto.GithubOauthProfileResponse;
import nextstep.member.application.dto.GithubAccessTokenRequest;
import nextstep.member.application.dto.GithubOauthAccessTokenResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface GithubClient {

    @POST
    Call<GithubOauthAccessTokenResponse> callAccessTokenApi(
            @Url String url,
            @Body GithubAccessTokenRequest githubAccessTokenRequest
    );

    @GET
    Call<GithubOauthProfileResponse> callProfileApi(@Url String loginUrl, @Header("Authorization") String token);
}
