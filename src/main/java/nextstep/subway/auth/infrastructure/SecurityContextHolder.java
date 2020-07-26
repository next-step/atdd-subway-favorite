package nextstep.subway.auth.infrastructure;

public class SecurityContextHolder {

    private static final ThreadLocal<SecurityContext> contextHolder;

    static {
        contextHolder = new ThreadLocal<>();
    }

    public static void clearContext() {
        contextHolder.remove();
    }

    public static SecurityContext getContext() {
        SecurityContext ctx = contextHolder.get();

        if (ctx == null) {
            ctx = SecurityContext.EMPTY_CONTEXT;
            contextHolder.set(ctx);
        }

        return ctx;
    }

    public static void setContext(SecurityContext context) {
        if (context != null) {
            contextHolder.set(context);
        }
    }
}
