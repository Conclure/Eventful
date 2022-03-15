package me.conclure.eventful.command;

import com.google.common.collect.UnmodifiableIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;

public interface CommandArguments extends Iterable<String> {
    static CommandArguments create(String[] args) {
        return new CommandArgumentsImpl(args);
    }

    String get(int index);

    boolean hasIndex(int index);

    int length();

    @NotNull
    @Override
    UnmodifiableIterator<String> iterator();
}
