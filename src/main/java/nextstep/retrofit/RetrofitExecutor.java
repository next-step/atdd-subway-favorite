package nextstep.retrofit;

import java.io.IOException;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import nextstep.exception.CannotInstanceException;
import retrofit2.Call;

@Slf4j
public class RetrofitExecutor {
    private RetrofitExecutor() {
        throw new CannotInstanceException();
    }

    public static <T> T execute(Call<T> call, Supplier<? extends RuntimeException> connectionFailExceptionSupplier) {
        try {
            return call.execute().body();
        } catch (IOException e) {
            log.error("Network Connection Failed", e);
            throw connectionFailExceptionSupplier.get();
        }
    }
}
