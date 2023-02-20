package nextstep.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {

    @NotBlank(message = "email은 필수값 입니다.")
    private String email;

    @NotBlank(message = "password는 필수값 입니다.")
    private String password;
}
