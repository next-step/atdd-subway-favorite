package nextstep.subway.member.domain;


import nextstep.subway.member.dto.MemberRequest;

public class LoginMember extends User{
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
        super(email, password);
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
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

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeAge(Integer age) {
        this.age = age;
    }
}
