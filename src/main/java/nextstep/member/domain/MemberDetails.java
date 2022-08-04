package nextstep.member.domain;

import nextstep.auth.userdetails.UserDetails;

import java.util.List;

public class MemberDetails implements UserDetails {
    private String email;
    private String password;
    private List<String> authorities;

    public MemberDetails() {
    }

    public MemberDetails(String email, String password, List<String> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static MemberDetails of(Member member) {
        return new MemberDetails(member.getEmail(), member.getPassword(), member.getRoles());
    }

    public static MemberDetails of(String email, List<String> authorities) {
        return new MemberDetails(email, null, authorities);
    }

    public static MemberDetails guest() {
        return new MemberDetails();
    }

    @Override
    public String getPrincipal() {
        return this.email;
    }

    @Override
    public String getCredential() {
        return this.password;
    }

    @Override
    public List<String> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
