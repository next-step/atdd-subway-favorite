package nextstep.subway.member.domain;


import nextstep.subway.auth.application.UserDetails;

public class LoginMember implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private Integer age;

    public static LoginMember of(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }

    private LoginMember() {
    }

    public LoginMember(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
