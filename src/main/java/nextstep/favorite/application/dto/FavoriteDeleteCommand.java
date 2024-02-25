package nextstep.favorite.application.dto;

public class FavoriteDeleteCommand {
    private Long favorite;
    private String memberEmail;

    public FavoriteDeleteCommand() {
    }

    public FavoriteDeleteCommand(Long favorite, String memberEmail) {
        this.favorite = favorite;
        this.memberEmail = memberEmail;
    }

    public Long getFavorite() {
        return favorite;
    }

    public String getMember() {
        return memberEmail;
    }
}
