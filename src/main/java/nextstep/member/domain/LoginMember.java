package nextstep.member.domain;


import nextstep.auth.authentication.domain.UserDetail;

public class LoginMember implements UserDetail {
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

    @Override
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public Integer getAge() {
        return age;
    }

    public String getPassword() {
        return password;
    }
}
