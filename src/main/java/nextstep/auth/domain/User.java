package nextstep.auth.domain;

public class User {

    private Long id;
    private String email;
    private int age;

    public User(Long id, String email, int age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
