package nextstep.auth.unit;

public final class TokenGenerator {

    private static final String SEPARATOR = ".";
    private static final String HEADER = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    private static final String PAYLOAD = "eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9";
    private static final String SIGNATURE = "ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    public static String getToken() {
        return HEADER + SEPARATOR + PAYLOAD + SEPARATOR + SIGNATURE;
    }
}
