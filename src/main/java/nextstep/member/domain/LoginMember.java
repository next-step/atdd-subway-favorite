package nextstep.member.domain;


import nextstep.auth.authentication.User;

public class LoginMember extends User {
    private Integer age;

    public static LoginMember of(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }

    public LoginMember(Long id, String email, String password, Integer age) {
        super(id, email, password);
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }
}
