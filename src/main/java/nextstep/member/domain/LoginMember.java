package nextstep.member.domain;


import java.util.Objects;

public class LoginMember {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginMember that = (LoginMember) o;
        return id.equals(that.id) && email.equals(that.email) && password.equals(that.password) && Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, age);
    }
}
