package nextstep.subway.auth.infrastructure;

public class UserDetails {

    private Long id;
    private String principal;
    private String credential;

    public UserDetails(Long id, String principal, String credential) {
        this.id = id;
        this.principal = principal;
        this.credential = credential;
    }

    public static UserDetails of(Long id, String principal, String credential) {
        return new UserDetails(id, principal, credential);
    }

    public boolean checkCredential(String credential) {
        return this.credential.equals(credential);
    }

    public String getPrincipal() {
        return principal;
    }

    public String getCredential() {
        return credential;
    }

    public Long getId() {
        return id;
    }
}
