package nextstep.favorite.application.dto;

public class FavoriteFindQuery {
    private String memberEmail;

    public FavoriteFindQuery(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getMemberEmail() {
        return memberEmail;
    }
}
