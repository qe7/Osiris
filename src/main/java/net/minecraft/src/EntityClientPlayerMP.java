package net.minecraft.src;

import io.github.qe7.Osiris;
import io.github.qe7.events.impl.player.MotionEvent;
import io.github.qe7.events.impl.player.PostMotionEvent;
import net.minecraft.client.Minecraft;

public class EntityClientPlayerMP extends EntityPlayerSP {
    public NetClientHandler sendQueue;

    /**
     * Tick counter that resets every 20 ticks, used for sending inventory updates
     */
    private int inventoryUpdateTickCounter;
    private double oldPosX;

    /**
     * Old Minimum Y of the bounding box
     */
    private double oldMinY;
    private double oldPosY;
    private double oldPosZ;
    private float oldRotationYaw;
    private float oldRotationPitch;

    /**
     * Check if was on ground last update
     */
    private boolean wasOnGround;

    /**
     * should the player stop sneaking?
     */
    private boolean shouldStopSneaking;
    private boolean wasSneaking;

    /**
     * The time since the client player moved
     */
    private int timeSinceMoved;

    /**
     * has the client player's health been set?
     */
    private boolean hasSetHealth;

    public EntityClientPlayerMP(Minecraft par1Minecraft, World par2World, Session par3Session, NetClientHandler par4NetClientHandler) {
        super(par1Minecraft, par2World, par3Session, 0);
        inventoryUpdateTickCounter = 0;
        wasOnGround = false;
        shouldStopSneaking = false;
        wasSneaking = false;
        timeSinceMoved = 0;
        hasSetHealth = false;
        sendQueue = par4NetClientHandler;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
        return false;
    }

    /**
     * Heal living entity (param: amount of half-hearts)
     */
    public void heal(int i) {
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        if (!worldObj.blockExists(MathHelper.floor_double(posX), 0, MathHelper.floor_double(posZ))) {
            return;
        } else {
            super.onUpdate();
            sendMotionUpdates();
            return;
        }
    }

    /**
     * Send updated motion and position information to the server
     */
    public void sendMotionUpdates() {
        if (this.isDead) {
            return;
        }

        if (inventoryUpdateTickCounter++ == 20) {
            inventoryUpdateTickCounter = 0;
        }

        boolean flag = isSprinting();

        if (flag != wasSneaking) {
            if (flag) {
                sendQueue.addToSendQueue(new Packet19EntityAction(this, 4));
            } else {
                sendQueue.addToSendQueue(new Packet19EntityAction(this, 5));
            }

            wasSneaking = flag;
        }

        boolean flag1 = isSneaking();

        if (flag1 != shouldStopSneaking) {
            if (flag1) {
                sendQueue.addToSendQueue(new Packet19EntityAction(this, 1));
            } else {
                sendQueue.addToSendQueue(new Packet19EntityAction(this, 2));
            }

            shouldStopSneaking = flag1;
        }

        final MotionEvent motionEvent = new MotionEvent(posX, boundingBox.minY, posY, posZ, rotationYaw, rotationPitch, onGround);
        Osiris.getInstance().getEventBus().post(motionEvent);

        double deltaX = motionEvent.getX() - oldPosX;
        double deltaMinY = motionEvent.getMinY() - oldMinY;
        double deltaY = motionEvent.getY() - oldPosY;
        double deltaZ = motionEvent.getZ() - oldPosZ;

        double deltaYaw = motionEvent.getYaw() - oldRotationYaw;
        double deltaPitch = motionEvent.getPitch() - oldRotationPitch;

        boolean hasPositionChanged = deltaMinY != 0.0D || deltaY != 0.0D || deltaX != 0.0D || deltaZ != 0.0D;
        boolean hasRotationChanged = deltaYaw != 0.0D || deltaPitch != 0.0D;

        if (ridingEntity != null) {
            if (hasRotationChanged) {
                sendQueue.addToSendQueue(new Packet11PlayerPosition(motionX, -999D, -999D, motionZ, motionEvent.isOnGround()));
            } else {
                sendQueue.addToSendQueue(new Packet13PlayerLookMove(motionX, -999D, -999D, motionZ, motionEvent.getYaw(), motionEvent.getPitch(), motionEvent.isOnGround()));
            }

            hasPositionChanged = false;
        } else if (hasPositionChanged && hasRotationChanged) {
            sendQueue.addToSendQueue(new Packet13PlayerLookMove(motionEvent.getX(), motionEvent.getMinY(), motionEvent.getY(), motionEvent.getZ(), motionEvent.getYaw(), motionEvent.getPitch(), motionEvent.isOnGround()));
            timeSinceMoved = 0;
        } else if (hasPositionChanged) {
            sendQueue.addToSendQueue(new Packet11PlayerPosition(motionEvent.getX(), motionEvent.getMinY(), motionEvent.getY(), motionEvent.getZ(), motionEvent.isOnGround()));
            timeSinceMoved = 0;
        } else if (hasRotationChanged) {
            sendQueue.addToSendQueue(new Packet12PlayerLook(motionEvent.getYaw(), motionEvent.getPitch(), motionEvent.isOnGround()));
            timeSinceMoved = 0;
        } else {
            sendQueue.addToSendQueue(new Packet10Flying(motionEvent.isOnGround()));

            if (wasOnGround != onGround || timeSinceMoved > 200) {
                timeSinceMoved = 0;
            } else {
                timeSinceMoved++;
            }
        }

        wasOnGround = onGround;

        if (hasPositionChanged) {
            oldPosX = motionEvent.getX();
            oldMinY = motionEvent.getMinY();
            oldPosY = motionEvent.getY();
            oldPosZ = motionEvent.getZ();
        }

        if (hasRotationChanged) {
            oldRotationYaw = motionEvent.getYaw();
            oldRotationPitch = motionEvent.getPitch();
        }

        final PostMotionEvent postMotionEvent = new PostMotionEvent();
        Osiris.getInstance().getEventBus().post(postMotionEvent);
    }

    /**
     * Called when player presses the drop item key
     */
    public EntityItem dropOneItem() {
        sendQueue.addToSendQueue(new Packet14BlockDig(4, 0, 0, 0, 0));
        return null;
    }

    /**
     * Joins the passed in entity item with the world. Args: entityItem
     */
    protected void joinEntityItemWithWorld(EntityItem entityitem) {
    }

    /**
     * Sends a chat message from the player. Args: chatMessage
     */
    public void sendChatMessage(String par1Str) {
        if (mc.ingameGUI.func_50013_c().size() == 0 || !((String) mc.ingameGUI.func_50013_c().get(mc.ingameGUI.func_50013_c().size() - 1)).equals(par1Str)) {
            mc.ingameGUI.func_50013_c().add(par1Str);
        }

        sendQueue.addToSendQueue(new Packet3Chat(par1Str));
    }

    /**
     * Swings the item the player is holding.
     */
    public void swingItem() {
        super.swingItem();
        sendQueue.addToSendQueue(new Packet18Animation(this, 1));
    }

    public void respawnPlayer() {
        sendQueue.addToSendQueue(new Packet9Respawn(dimension, (byte) worldObj.difficultySetting, worldObj.getWorldInfo().getTerrainType(), worldObj.getHeight(), 0));
    }

    /**
     * Deals damage to the entity. If its a EntityPlayer then will take damage from the armor first and then health
     * second with the reduced value. Args: damageAmount
     */
    protected void damageEntity(DamageSource par1DamageSource, int par2) {
        setEntityHealth(getHealth() - par2);
    }

    /**
     * sets current screen to null (used on escape buttons of GUIs)
     */
    public void closeScreen() {
        sendQueue.addToSendQueue(new Packet101CloseWindow(craftingInventory.windowId));
        inventory.setItemStack(null);
        super.closeScreen();
    }

    /**
     * Updates health locally.
     */
    public void setHealth(int par1) {
        if (hasSetHealth) {
            super.setHealth(par1);
        } else {
            setEntityHealth(par1);
            hasSetHealth = true;
        }
    }

    /**
     * Adds a value to a statistic field.
     */
    public void addStat(StatBase par1StatBase, int par2) {
        if (par1StatBase == null) {
            return;
        }

        if (par1StatBase.isIndependent) {
            super.addStat(par1StatBase, par2);
        }
    }

    /**
     * Used by NetClientHandler.handleStatistic
     */
    public void incrementStat(StatBase par1StatBase, int par2) {
        if (par1StatBase == null) {
            return;
        }

        if (!par1StatBase.isIndependent) {
            super.addStat(par1StatBase, par2);
        }
    }

    public void func_50009_aI() {
        sendQueue.addToSendQueue(new Packet202PlayerAbilities(capabilities));
    }
}
