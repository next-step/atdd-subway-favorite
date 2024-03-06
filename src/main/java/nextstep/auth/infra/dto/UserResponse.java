package nextstep.auth.infra.dto;

public class UserResponse {

    private Long id;
    private String email;
    private int age;

    public UserResponse(Long id, String email, int age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public UserResponse() {

    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
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
