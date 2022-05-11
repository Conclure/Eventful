package me.conclure.eventful.shared;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public final class Identifier implements CharSequence {
    private static final Pattern PATTERN;

    static {
        PATTERN = Pattern.compile("[a-z]+");
    }

    private final String value;

    private Identifier(String value) {
        this.value = value;
    }

    public static Identifier of(String token) {
        Objects.requireNonNull(token);
        token = token.trim().toLowerCase(Locale.ROOT);
        boolean matches = PATTERN.matcher(token).matches();
        if (!matches) {
            throw new IllegalArgumentException();
        }
        return new Identifier(token);
    }

    @Override
    public int length() {
        return this.value.length();
    }

    @Override
    public char charAt(int index) {
        return this.value.charAt(index);
    }

    @NotNull
    @Override
    public CharSequence subSequence(int start, int end) {
        return this.value.subSequence(start,end);
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }
}
