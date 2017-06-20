package tdl.auth.helpers;

import com.amazonaws.services.lambda.runtime.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LambdaExceptionLogger {

    public static void logException(Context context, Exception exception) {
        // Collect all stack traces
        List<String> theTrace = new ArrayList<>();

        Throwable throwable = exception;
        while (throwable != null) {
            theTrace.add(throwable.getClass().getSimpleName() + ": " + throwable.getMessage());
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            Arrays.stream(stackTrace).map(StackTraceElement::toString).forEach(theTrace::add);
            throwable = throwable.getCause();
        }

        //Log the stack traces
        context.getLogger().log(theTrace.stream().collect(Collectors.joining("\n")));
    }
}
