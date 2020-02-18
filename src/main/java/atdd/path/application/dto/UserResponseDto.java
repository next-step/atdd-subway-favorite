package atdd.path.application.dto;

public class UserResponseDto {
    private Long id;

    private String email;
    private String name;

    public UserResponseDto() {
    }

    public UserResponseDto(Long id,
                           String email,
                           String name) {

        this.id = id;
        this.email = email;
        this.name = name;

    }

    public static UserResponseDto of(Long id,
                                     String email,
                                     String name) {

        return new UserResponseDto(id, email, name);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

}
