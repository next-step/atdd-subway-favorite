package nextstep.domain.member.domain;

import nextstep.auth.authentication.LoginMember;

public class LoginMemberImpl extends Member implements LoginMember {
    private Long id;
    private String email;
    private String password;
    private Integer age;

    public static LoginMemberImpl of(Member member) {
        return new LoginMemberImpl(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }

    public LoginMemberImpl(Long id, String email, String password, Integer age) {
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

}
