package io.github.qe7.features.impl.modules.impl.misc;

import io.github.qe7.Osiris;
import io.github.qe7.events.impl.player.LivingUpdateEvent;
import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.features.impl.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.features.impl.modules.api.settings.impl.DoubleSetting;
import io.github.qe7.features.impl.modules.api.settings.impl.EnumSetting;
import io.github.qe7.features.impl.modules.api.settings.impl.interfaces.IEnumSetting;
import me.zero.alpine.listener.Listener;
import me.zero.alpine.listener.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;

public class AutoDisconnectModule extends Module {

    private final EnumSetting<Mode> mode = new EnumSetting<>("Mode", Mode.Health);
    private final BooleanSetting excludeFriends = new BooleanSetting("Exclude Friends", false).supplyIf(() -> (mode.getValue() != Mode.Health));
    private final BooleanSetting onlyEnemies = new BooleanSetting("Only Enemies", false).supplyIf(() -> (mode.getValue() != Mode.Health));
    private final DoubleSetting health = new DoubleSetting("Health", 14.0, 0.5, 20.0, 0.5f).supplyIf(() -> (mode.getValue() != Mode.Player));

    public AutoDisconnectModule() {
        super("Auto Disconnect", "Automatically disconnects the player under certain conditions", ModuleCategory.MISC);
    }

    @Subscribe
    public final Listener<LivingUpdateEvent> livingUpdateListener = new Listener<>(event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        switch (mode.getValue()) {
            case Health:
                if (mc.thePlayer.getHealth() <= health.getValue()) {
                    mc.theWorld.sendQuittingDisconnectingPacket();
                }
                break;
            case Player:
                for (Entity entity : mc.theWorld.loadedEntityList) {
                    if (entity instanceof EntityPlayer) {
                        EntityPlayer entityPlayer = (EntityPlayer) entity;
                        if (Osiris.getInstance().getRelationManager().isFriend(entityPlayer.username) && !this.excludeFriends.getValue()) {
                            mc.theWorld.sendQuittingDisconnectingPacket();
                            break;
                        }
                        if (!Osiris.getInstance().getRelationManager().isFriend(entityPlayer.username) && !Osiris.getInstance().getRelationManager().isEnemy(entityPlayer.username) && !this.onlyEnemies.getValue()) {
                            mc.theWorld.sendQuittingDisconnectingPacket();
                            break;
                        }
                        if (Osiris.getInstance().getRelationManager().isEnemy(entityPlayer.username)) {
                            mc.theWorld.sendQuittingDisconnectingPacket();
                            break;
                        }
                    }
                }
                break;
            case HealthAndPlayer:
                if (mc.thePlayer.getHealth() <= health.getValue()) {
                    mc.theWorld.sendQuittingDisconnectingPacket();
                } else {
                    for (Entity entity : mc.theWorld.loadedEntityList) {
                        if (entity instanceof EntityPlayer) {
                            EntityPlayer entityPlayer = (EntityPlayer) entity;
                            if (Osiris.getInstance().getRelationManager().isFriend(entityPlayer.username) && !this.excludeFriends.getValue()) {
                                mc.theWorld.sendQuittingDisconnectingPacket();
                                break;
                            }
                            if (!Osiris.getInstance().getRelationManager().isFriend(entityPlayer.username) && !Osiris.getInstance().getRelationManager().isEnemy(entityPlayer.username) && !this.onlyEnemies.getValue()) {
                                mc.theWorld.sendQuittingDisconnectingPacket();
                                break;
                            }
                            if (Osiris.getInstance().getRelationManager().isEnemy(entityPlayer.username)) {
                                mc.theWorld.sendQuittingDisconnectingPacket();
                                break;
                            }
                        }
                    }
                }
                break;
        }
    });

    private enum Mode implements IEnumSetting {
        Health("Health"), Player("Player"), HealthAndPlayer("Both");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Enum<?> getEnum(String name) {
            return Mode.valueOf(name);
        }
    }
}