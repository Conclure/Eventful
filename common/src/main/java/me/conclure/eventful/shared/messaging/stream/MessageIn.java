package me.conclure.eventful.shared.messaging.stream;

import me.conclure.eventful.shared.messaging.MessageException;

import java.io.DataInput;
import java.io.IOException;

public class MessageIn {
    private final DataInput input;

    public MessageIn(DataInput input) {
        this.input = input;
    }

    public void raiseException(String message) {
        throw this.exception(message);
    }

    public RuntimeException exception(String message) {
        return new MessageException(message);
    }

    public String readString() {
        try {
            return this.input.readUTF();
        } catch (IOException e) {
            throw new MessageException(e);
        }
    }

    public int readInt() {
        try {
            return this.input.readInt();
        } catch (IOException e) {
            throw new MessageException(e);
        }
    }
}
