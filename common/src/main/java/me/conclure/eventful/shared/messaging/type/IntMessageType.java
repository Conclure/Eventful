package me.conclure.eventful.shared.messaging.type;

import me.conclure.eventful.shared.messaging.Message;
import me.conclure.eventful.shared.messaging.MessageDraft;
import me.conclure.eventful.shared.messaging.stream.MessageIn;
import me.conclure.eventful.shared.messaging.stream.MessageOut;

public class IntMessageType extends MessageType<Integer> {
    public IntMessageType(int id) {
        super(id);
    }

    @Override
    public MessageDraft<Integer> toMessage(MessageIn in) {
        return this.create(in.readInt());
    }

    @Override
    public void fromMessage(Message<Integer> message, MessageOut out) {
        out.writeInt(message.content());
    }
}
