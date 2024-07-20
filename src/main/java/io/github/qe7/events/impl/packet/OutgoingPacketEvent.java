package io.github.qe7.events.impl.packet;

import io.github.qe7.events.api.types.CancellableEvent;
import net.minecraft.src.Packet;

public final class OutgoingPacketEvent extends CancellableEvent {

    private final Packet packet;

    public OutgoingPacketEvent(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }
}
