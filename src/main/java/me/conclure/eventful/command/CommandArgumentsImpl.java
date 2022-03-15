package me.conclure.eventful.command;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class CommandArgumentsImpl implements CommandArguments {
    private final String[]args;

    public CommandArgumentsImpl(String[] args) {
        this.args = args.clone();
    }

    @Override
    public String get(int index) {
        return this.args[index];
    }

    @Override
    public boolean hasIndex(int index) {
        return this.args.length > index;
    }

    @Override
    public int length() {
        return this.args.length;
    }

    @NotNull
    @Override
    public UnmodifiableIterator<String> iterator() {
        return Iterators.forArray(this.args);
    }
}
