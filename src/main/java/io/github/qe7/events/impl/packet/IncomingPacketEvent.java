package io.github.qe7.events.impl.packet;

import lombok.Getter;
import me.zero.alpine.event.CancellableEvent;
import net.minecraft.src.Packet;

@Getter
public final class IncomingPacketEvent extends CancellableEvent {

    private final Packet packet;

    public IncomingPacketEvent(Packet packet) {
        this.packet = packet;
    }

}
