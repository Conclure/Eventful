package me.conclure.eventful.shared.messaging.type;

import me.conclure.eventful.shared.messaging.Message;
import me.conclure.eventful.shared.messaging.stream.MessageIn;
import me.conclure.eventful.shared.messaging.stream.MessageOut;

public class StringMessageType extends MessageType<String> {

    public StringMessageType(int id) {
        super(id);
    }

    @Override
    public Message.Builder<String> toMessage(MessageIn in) {
        return this.builder(in.readString());
    }

    @Override
    public void fromMessage(Message<String> message, MessageOut out) {
        out.writeString(message.unwrap());
    }
}
