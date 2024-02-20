package nextstep.auth.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GithubEmailResponse {
    private String email;
    private boolean primary;
    private boolean verified;
    private String visibility;

    public boolean isVerifiedPrimaryEmail() {
        return primary && verified;
    }
}
