package nextstep.auth.application.dto;

public class OAuth2Response {

    private String email;
    private Integer age;

    public OAuth2Response() {
    }

    public OAuth2Response(final String email, final Integer age) {
        this.email = email;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
