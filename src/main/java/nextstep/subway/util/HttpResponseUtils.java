package nextstep.subway.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Supplier;

public final class HttpResponseUtils {

    public static void write(HttpServletResponse response, Supplier<String> bodyContentSupplier) {
        try (final PrintWriter writer = response.getWriter()) {
            writer.write(bodyContentSupplier.get());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
