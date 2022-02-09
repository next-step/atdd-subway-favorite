package nextstep.auth.manager;

import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;

public class UserMember {
    private Long id;
    private String email;
    private String password;
    private Integer age;

    public static UserMember of(Member member) {
        return new UserMember(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }

    public UserMember(Long id, String email, String password, Integer age) {
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
