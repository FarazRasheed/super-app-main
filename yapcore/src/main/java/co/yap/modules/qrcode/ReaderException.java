package co.yap.modules.qrcode;

public abstract class ReaderException extends Exception {

    // disable stack traces when not running inside test units
    protected static final boolean isStackTrace =
            System.getProperty("surefire.test.class.path") != null;
    protected static final StackTraceElement[] NO_TRACE = new StackTraceElement[0];

    ReaderException() {
        // do nothing
    }

    ReaderException(Throwable cause) {
        super(cause);
    }

    // Prevent stack traces from being taken
    @Override
    public final synchronized Throwable fillInStackTrace() {
        return null;
    }

}
