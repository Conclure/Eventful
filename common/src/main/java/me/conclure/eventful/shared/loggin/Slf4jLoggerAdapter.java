package me.conclure.eventful.shared.loggin;

public class Slf4jLoggerAdapter implements LoggerAdapter {
    private final org.slf4j.Logger loggerDelegate;

    public Slf4jLoggerAdapter(org.slf4j.Logger delegatingLogger) {
        this.loggerDelegate = delegatingLogger;
    }

    @Override
    public void error(String msg) {
        this.loggerDelegate.error(msg);
    }

    @Override
    public void errorf(String msg, Object... args) {
        this.error(String.format(msg, args));
    }

    @Override
    public void error(Throwable cause) {
        this.loggerDelegate.error(cause.getMessage(), cause);
    }

    @Override
    public void error(Throwable cause, String msg) {
        this.loggerDelegate.error(msg, cause);
    }

    @Override
    public void errorf(Throwable cause, String msg, Object... args) {
        this.error(cause, String.format(msg, args));
    }

    @Override
    public void warn(String msg) {
        this.loggerDelegate.warn(msg);
    }

    @Override
    public void warnf(String msg, Object... args) {
        this.warn(String.format(msg, args));
    }

    @Override
    public void warn(Throwable cause) {
        this.loggerDelegate.warn(cause.getMessage(), cause);
    }

    @Override
    public void warn(Throwable cause, String msg) {
        this.loggerDelegate.warn(msg, cause);
    }

    @Override
    public void warnf(Throwable cause, String msg, Object... args) {
        this.warn(cause, String.format(msg, args));
    }

    @Override
    public void info(String msg) {
        this.loggerDelegate.info(msg);
    }

    @Override
    public void infof(String msg, Object... args) {
        this.info(String.format(msg, args));
    }

    @Override
    public void info(Throwable cause) {
        this.loggerDelegate.info(cause.getMessage(), cause);
    }

    @Override
    public void info(Throwable cause, String msg) {
        this.loggerDelegate.info(msg, cause);
    }

    @Override
    public void infof(Throwable cause, String msg, Object... args) {
        this.info(cause, String.format(msg, args));
    }

    @Override
    public void debug(String msg) {
        this.loggerDelegate.debug(msg);
    }

    @Override
    public void debugf(String msg, Object... args) {
        this.debug(String.format(msg, args));
    }

    @Override
    public void debug(Throwable cause) {
        this.loggerDelegate.debug(cause.getMessage(), cause);
    }

    @Override
    public void debug(Throwable cause, String msg) {
        this.loggerDelegate.debug(msg, cause);
    }

    @Override
    public void debugf(Throwable cause, String msg, Object... args) {
        this.debug(cause, String.format(msg, args));
    }
}
