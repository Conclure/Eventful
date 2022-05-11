package me.conclure.eventful.shared.messaging.type;

import me.conclure.eventful.shared.messaging.Message;
import me.conclure.eventful.shared.messaging.stream.MessageIn;
import me.conclure.eventful.shared.messaging.stream.MessageOut;

public class PingMessageType extends MessageType<String[]> {

    public PingMessageType(int id) {
        super(id);
    }

    @Override
    public Message.Builder<String[]> toMessage(MessageIn in) {
        int length = in.readInt();
        String[] arguments = new String[length];
        for (int i = 0; i < length; i++) {
            arguments[i] = in.readString();
        }
        return this.builder(arguments);
    }

    @Override
    public void fromMessage(Message<String[]> message, MessageOut out) {
        String[] arguments = message.unwrap();
        out.writeInt(arguments.length);
        for (String argument : arguments) {
            out.writeString(argument);
        }
    }
}