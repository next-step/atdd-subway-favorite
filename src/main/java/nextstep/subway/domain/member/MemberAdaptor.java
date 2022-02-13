package nextstep.subway.domain.member;


import nextstep.auth.model.authentication.UserDetails;
import nextstep.utils.exception.AuthenticationException;

public class MemberAdaptor implements UserDetails {
    private Long id;
    private String username;
    private String credential;

    private MemberAdaptor(Long id, String username, String credential) {
        this.id = id;
        this.username = username;
        this.credential = credential;
    }

    public static MemberAdaptor of(Member member) {
        return new MemberAdaptor(member.getId(), member.getEmail(), member.getPassword());
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getCredential() {
        return credential;
    }

    @Override
    public boolean validateCredential(String targetPassword) {
        if (this.credential.equals(targetPassword)) {
            this.credential = null;
            return true;
        }

        throw new AuthenticationException();
    }
}
