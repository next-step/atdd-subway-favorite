package nextstep.member.infrastructure.dto;

public class ProfileDto {

    private final String email;

    private ProfileDto(String email) {
        this.email = email;
    }

    public static ProfileDto from(String email) {
        return new ProfileDto(
            email
        );
    }

    public String getEmail() {
        return email;
    }
}
