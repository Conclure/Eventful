package me.conclure.eventful.shared;

import java.io.PrintStream;
import java.io.PrintWriter;

public final class UnreachableCodeError extends AssertionError {
    public static final AssertionError INSTANCE = new UnreachableCodeError();

    private UnreachableCodeError() {
        super("Unreachable code");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        throw new AssertionError();
    }

    @Override
    public String getMessage() {
        throw new AssertionError();
    }

    @Override
    public String getLocalizedMessage() {
        throw new AssertionError();
    }

    @Override
    public synchronized Throwable getCause() {
        throw new AssertionError();
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        throw new AssertionError();
    }

    @Override
    public String toString() {
        throw new AssertionError();
    }

    @Override
    public void printStackTrace() {
        throw new AssertionError();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        throw new AssertionError();
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        throw new AssertionError();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        throw new AssertionError();
    }

    @Override
    public void setStackTrace(StackTraceElement[] stackTrace) {
        throw new AssertionError();
    }
}
