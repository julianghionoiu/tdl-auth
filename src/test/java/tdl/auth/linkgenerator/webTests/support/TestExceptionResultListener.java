package tdl.auth.linkgenerator.webTests.support;

import com.hotwire.imageassert.listener.Result;
import com.hotwire.imageassert.listener.ResultListener;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

public final class TestExceptionResultListener implements ResultListener {

    private String message;

    public TestExceptionResultListener(String message) {
        this.message = message;
    }

    public void report(@NotNull Result result) {
        Intrinsics.checkParameterIsNotNull(result, "result");
        if (result.getMismatch() > result.getThreshold()) {
            throw new AssertionError(message);
        }
    }
}
