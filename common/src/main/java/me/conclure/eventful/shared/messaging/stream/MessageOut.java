package me.conclure.eventful.shared.messaging.stream;

import me.conclure.eventful.shared.messaging.MessageException;

import java.io.DataOutput;
import java.io.IOException;

public class MessageOut {
    private final DataOutput output;

    public MessageOut(DataOutput output) {
        this.output = output;
    }

    public void raiseException(String message) {
        throw new MessageException(message);
    }

    public void writeString(String string) {
        try {
            this.output.writeUTF(string);
        } catch (IOException e) {
            throw new MessageException(e);
        }
    }

    public void writeInt(int integer) {
        try {
            this.output.writeInt(integer);
        } catch (IOException e) {
            throw new MessageException(e);
        }
    }
}
