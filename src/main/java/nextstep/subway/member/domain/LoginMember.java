package nextstep.subway.member.domain;


import nextstep.subway.auth.domain.User;

public class LoginMember implements User {
    private Long id;
    private String email;
    private String password;
    private Integer age;

    public static LoginMember of(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }

    public void update(LoginMember loginMember){
        this.email = loginMember.getEmail();
        this.password = loginMember.getPassword();
        this.age = loginMember.getAge();
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

    @Override
    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean checkPassword(String password) {
        return false;
    }
}
