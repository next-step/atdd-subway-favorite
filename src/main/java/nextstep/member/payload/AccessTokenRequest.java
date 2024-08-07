package nextstep.member.payload;


public class AccessTokenRequest {

    private String client_id;
    private String client_secret;
    private String code;

    public String getClient_id() {
        return client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }


    public String getCode() {
        return code;
    }

    public void setClient_id(final String client_id) {
        this.client_id = client_id;
    }

    public void setClient_secret(final String client_secret) {
        this.client_secret = client_secret;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public static AuthorizeRequestBuilder builder() {
        return new AuthorizeRequestBuilder();
    }

    private AccessTokenRequest(final String client_id, final String client_secret, final String code) {
        this.client_id = client_id;
        this.client_secret = client_secret;
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
