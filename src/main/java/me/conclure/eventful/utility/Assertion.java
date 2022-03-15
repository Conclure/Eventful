package me.conclure.eventful.utility;

public class Assertion {
    public static void raise() throws AssertionError{
        throw new AssertionError();
    }

    public static void raise(String message) throws AssertionError{
        throw new AssertionError(message);
    }
}
