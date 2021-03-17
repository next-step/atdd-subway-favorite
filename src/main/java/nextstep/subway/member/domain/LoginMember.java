package nextstep.subway.member.domain;


import nextstep.subway.auth.application.UserDetails;
import org.apache.commons.lang3.StringUtils;

public class LoginMember implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private Integer age;

    public static LoginMember of(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }

    public LoginMember(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public boolean checkCredentials(Object credentials) {
        return StringUtils.equals(credentials.toString(), password);
    }
}
