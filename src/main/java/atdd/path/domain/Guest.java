package atdd.path.domain;

public class Guest extends Member {

    public static final Member GUEST_MEMBER = new Guest();

    @Override
    public boolean isGuest() {
        return true;
    }

}
