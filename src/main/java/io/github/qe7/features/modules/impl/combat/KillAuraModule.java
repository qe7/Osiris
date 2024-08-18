package io.github.qe7.features.modules.impl.combat;

import io.github.qe7.Osiris;
import io.github.qe7.events.api.EventLink;
import io.github.qe7.events.api.Listener;
import io.github.qe7.events.impl.player.MotionEvent;
import io.github.qe7.events.impl.player.PostMotionEvent;
import io.github.qe7.events.impl.render.RenderItemEvent;
import io.github.qe7.features.modules.api.Module;
import io.github.qe7.features.modules.api.enums.ModuleCategory;
import io.github.qe7.features.modules.api.settings.impl.BooleanSetting;
import io.github.qe7.features.modules.api.settings.impl.DoubleSetting;
import io.github.qe7.utils.local.PacketUtility;
import io.github.qe7.utils.math.Stopwatch;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.List;

public class KillAuraModule extends Module {

    // range settings
    private final DoubleSetting range = new DoubleSetting("Range", 4.2, 1.0, 6.0, 0.1);
    // timing settings
    private final BooleanSetting tick = new BooleanSetting("Tick", true);
    private final DoubleSetting aps = new DoubleSetting("APS", 10.0, 1.0, 20.0, 1.0).supplyIf(() -> !tick.getValue());
    // target settings
    private final BooleanSetting players = new BooleanSetting("Players", true);
    private final BooleanSetting animals = new BooleanSetting("Animals", false);
    private final BooleanSetting monsters = new BooleanSetting("Mob", false);
    // rotation settings
    private final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    // sanity settings
    private final BooleanSetting disableOnDeath = new BooleanSetting("Disable on death", true);
    // auto block settings
    private final BooleanSetting autoBlock = new BooleanSetting("Auto block", true);

    private final Stopwatch stopwatch = new Stopwatch();

    private final List<EntityLiving> targets = new ArrayList<>();

    private boolean shouldBlock = false;

    public KillAuraModule() {
        super("Kill Aura", "Automatically attacks entities", ModuleCategory.COMBAT);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        this.stopwatch.reset();
        this.targets.clear();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        final Minecraft mc = Minecraft.getMinecraft();

        if (shouldBlock) {
            if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                PacketUtility.sendPacket(new Packet15Place());
            }
            shouldBlock = false;
        }
    }

    @EventLink
    public final Listener<MotionEvent> motionListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld == null) return;
        if (mc.thePlayer == null) return;

        // if the player is dead, disable the module
        if (mc.thePlayer.isDead && disableOnDeath.getValue()) {
            this.setEnabled(false);
            return;
        }

        handleTargets(mc);

        if (this.targets.isEmpty()) return;

        // sort the targets by distance
        this.targets.sort((a, b) -> {
            double distanceA = mc.thePlayer.getDistanceToEntity(a);
            double distanceB = mc.thePlayer.getDistanceToEntity(b);

            return Double.compare(distanceA, distanceB);
        });

        // get the closest target
        final EntityLiving target = this.targets.stream().min((a, b) -> {
            double distanceA = mc.thePlayer.getDistanceToEntity(a);
            double distanceB = mc.thePlayer.getDistanceToEntity(b);

            return Double.compare(distanceA, distanceB);
        }).orElse(null);

        // if the player is out of range, return
        if (target == null) return;

        if (target.isDead || target.getHealth() <= 0.0f) {
            this.targets.remove(target);
            return;
        }

        if (mc.thePlayer.getDistanceToEntity(target) > this.range.getValue()) return;

        // if the player is out of FOV, return
        if (this.rotate.getValue()) {
            float[] rotations = getRotations(target, mc);

            event.setYaw(rotations[0]);
            event.setPitch(rotations[1]);
        }

        // auto block
        if (this.autoBlock.getValue()) {
            if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                PacketUtility.sendPacket(new Packet15Place(0, 0, 0, 0, mc.thePlayer.inventory.getCurrentItem()));
                shouldBlock = true;
            }
        }

        // if the player is out of APS, return
        if (this.tick.getValue()) {
            // check every tick if the target is @ hurt time 0
            if (this.stopwatch.elapsed(50)) {
                if (target.hurtTime == 0) {
                    mc.playerController.attackEntity(mc.thePlayer, target);
                    mc.thePlayer.swingItem();
                }
                this.stopwatch.reset();
            }
        } else {
            if (this.stopwatch.elapsed((long) (1000 / this.aps.getValue()))) {
                mc.playerController.attackEntity(mc.thePlayer, target);
                mc.thePlayer.swingItem();
                this.stopwatch.reset();
            }
        }
    };

    @EventLink
    public final Listener<PostMotionEvent> postMotionListener = event -> {
        final Minecraft mc = Minecraft.getMinecraft();

        if (shouldBlock) {
            if (mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                PacketUtility.sendPacket(new Packet14BlockDig(5, 0, 0, 0, 0));
            }
            shouldBlock = false;
        }
    };

    @EventLink
    public final Listener<RenderItemEvent> renderItemListener = event -> {
        if (this.autoBlock.getValue() && !this.targets.isEmpty()) {
            event.setUseItemCount(1);
            event.setAction(EnumAction.block);
        }
    };

    private void handleTargets(final Minecraft mc) {
        this.targets.clear();
        for (Object entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityLiving)) continue;

            final EntityLiving living = (EntityLiving) entity;

            if (living == mc.thePlayer) continue;
            if (living.isDead) continue;

            if (living.getDistanceToEntity(mc.thePlayer) > this.range.getValue()) continue;

            if (this.players.getValue() && living instanceof EntityPlayer) {
                if (Osiris.getInstance().getRelationManager().isFriend(((EntityPlayer) living).username)) continue;
                if (Osiris.getInstance().getRelationManager().isEnemy(((EntityPlayer) living).username)) {
                    this.targets.add(living);
                    return;
                }
                this.targets.add(living);
            }

            if (this.animals.getValue() && living instanceof EntityAnimal) {
                this.targets.add(living);
            }

            if (this.monsters.getValue() && living instanceof EntityMob) {
                this.targets.add(living);
            }
        }
    }

    private float[] getRotations(final Entity entity, final Minecraft mc) {
        double x = entity.posX - mc.thePlayer.posX;
        double y = entity.posY + entity.getEyeHeight() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double z = entity.posZ - mc.thePlayer.posZ;

        double distance = Math.sqrt(x * x + z * z);

        float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        float pitch = (float) -Math.toDegrees(Math.atan2(y, distance));

        return new float[]{yaw, pitch};
    }
}
