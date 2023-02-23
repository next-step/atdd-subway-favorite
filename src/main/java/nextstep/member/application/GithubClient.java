package nextstep.member.application;

public interface GithubClient {
    String getAccessTokenFromGithub(String code);
}
