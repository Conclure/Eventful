package me.conclure.eventful.shared.loggin;

public interface LoggerAdapter {
    void error(String msg);

    void errorf(String msg, Object... args);

    void error(Throwable cause);

    void error(Throwable cause, String msg);

    void errorf(Throwable cause, String msg, Object... args);

    void warn(String msg);

    void warnf(String msg, Object... args);

    void warn(Throwable cause);

    void warn(Throwable cause, String msg);

    void warnf(Throwable cause, String msg, Object... args);

    void info(String msg);

    void infof(String msg, Object... args);

    void info(Throwable cause);

    void info(Throwable cause, String msg);

    void infof(Throwable cause, String msg, Object... args);

    void debug(String msg);

    void debugf(String msg, Object... args);

    void debug(Throwable cause);

    void debug(Throwable cause, String msg);

    void debugf(Throwable cause, String msg, Object... args);
}
