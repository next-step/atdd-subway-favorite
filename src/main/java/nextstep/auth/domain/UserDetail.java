package nextstep.auth.domain;

public class UserDetail {
    private Long id;
    private String email;
    private String password;
    private int age;

    public UserDetail(Long id, String email, String password, int age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public UserDetail(String email, String password, int age) {
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

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }
}
