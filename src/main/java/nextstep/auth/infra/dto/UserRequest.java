package nextstep.auth.infra.dto;

public class UserRequest {

    private String email;
    private String password;
    private int age;


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UserRequest(String email, String password, int age) {
        this.email = email;
        this.password = password;
        this.age = age;


    }
}
