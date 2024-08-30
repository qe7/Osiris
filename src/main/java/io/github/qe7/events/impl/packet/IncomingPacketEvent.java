package io.github.qe7.events.impl.packet;

import io.github.qe7.events.api.types.CancellableEvent;
import lombok.Getter;
import net.minecraft.src.Packet;

@Getter
public final class IncomingPacketEvent extends CancellableEvent {

    private final Packet packet;

    public IncomingPacketEvent(Packet packet) {
        this.packet = packet;
    }

}
