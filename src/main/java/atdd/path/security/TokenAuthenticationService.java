package atdd.path.security;

public class TokenAuthenticationService {
    private static final String SALT = "63B75D39E3F6BFE72263F7C1145AC22E";
    private static final String HEADER_STRING = "Authorization";


    public byte[] generateKey(String salt) {
        return salt.getBytes();
    }
}
