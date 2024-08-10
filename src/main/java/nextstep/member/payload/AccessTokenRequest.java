package nextstep.member.payload;


import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessTokenRequest {

    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("client_secret")
    private String clientSecret;
    private String code;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }


    public String getCode() {
        return code;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(final String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public static AuthorizeRequestBuilder builder() {
        return new AuthorizeRequestBuilder();
    }

    private AccessTokenRequest(final String clientId, final String clientSecret, final String code) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
    }

    public static class AuthorizeRequestBuilder {
        private String clientId;
        private String clientSecret;
        private String code;


        public AuthorizeRequestBuilder clientId(final String clientId) {
            this.clientId = clientId;
            return this;
        }

        public AuthorizeRequestBuilder clientSecret(final String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public AuthorizeRequestBuilder code(final String code) {
            this.code = code;
            return this;
        }

        public AccessTokenRequest build() {
            return new AccessTokenRequest(clientId, clientSecret, code);
        }

    }


}
