package nextstep.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "subway.app")
public class AppProperties {

    private GithubProperties github;
    private UserService userService;
    private AuthService authService;


    public AppProperties() {
    }

    public AppProperties(GithubProperties github) {
        this.github = github;
    }

    public GithubProperties getGithub() {
        return github;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setGithub(GithubProperties github) {
        this.github = github;
    }

    public UserService getUserService() {
        return userService;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public static class GithubProperties {

        private String clientId;
        private String clientSecret;
        private String accessTokenUri;
        private String userInfoUri;

        public GithubProperties(String clientId, String clientSecret,
            String accessTokenUri, String userInfoUri) {
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            this.accessTokenUri = accessTokenUri;
            this.userInfoUri = userInfoUri;
        }

        public GithubProperties() {
        }

        public String getClientId() {
            return clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public String getAccessTokenUri() {
            return accessTokenUri;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public void setAccessTokenUri(String accessTokenUri) {
            this.accessTokenUri = accessTokenUri;
        }

        public void setUserInfoUri(String userInfoUri) {
            this.userInfoUri = userInfoUri;
        }

        public String getUserInfoUri() {
            return userInfoUri;
        }
    }

    @ConstructorBinding
    public static class UserService {

        private String meUrl;
        private String createUrl;

        public UserService(String meUrl, String createUrl) {
            this.meUrl = meUrl;
            this.createUrl = createUrl;
        }

        public UserService() {
        }

        public String getMeUrl() {
            return meUrl;
        }

        public String getCreateUrl() {
            return createUrl;
        }

        public void setMeUrl(String meUrl) {
            this.meUrl = meUrl;
        }

        public void setCreateUrl(String createUrl) {
            this.createUrl = createUrl;
        }
    }

    public static class AuthService {

        private String tokenUrl;

        public AuthService(String tokenUrl) {
            this.tokenUrl = tokenUrl;
        }

        public AuthService() {
        }

        public String getTokenUrl() {
            return tokenUrl;
        }

        public void setTokenUrl(String tokenUrl) {
            this.tokenUrl = tokenUrl;
        }
    }
}
