package atdd.path.web.dto;

public class UserCreateRequestDto {
    private String email;
    private String name;
    private String password;

    public UserCreateRequestDto() {
    }

    public UserCreateRequestDto(String email,
                                String name,
                                String password) {

        this.email = email;
        this.name = name;
        this.password = password;
    }

    public static UserCreateRequestDto of(String email,
                                          String name,
                                          String password) {

        return new UserCreateRequestDto(email, name, password);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
    
}
