package nextstep.common.retrofit;

import lombok.extern.slf4j.Slf4j;
import nextstep.common.exception.CannotInstanceException;
import retrofit2.Call;

import java.io.IOException;
import java.util.function.Supplier;

@Slf4j
public class RetrofitExecutor {

    private RetrofitExecutor() {
        throw new CannotInstanceException();
    }

    public static <T> T execute(Call<T> call, Supplier<? extends RuntimeException> connectionExceptionSupplier) {
        try {
            return call.execute().body();
        } catch (IOException e) {
            log.error("GITHUB 서버와 통신에 실패하였습니다.", e);
            throw connectionExceptionSupplier.get();
        }
    }
}
