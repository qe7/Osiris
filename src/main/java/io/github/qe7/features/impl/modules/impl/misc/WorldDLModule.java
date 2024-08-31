package io.github.qe7.features.impl.modules.impl.misc;

import io.github.qe7.features.impl.modules.api.Module;
import io.github.qe7.features.impl.modules.api.enums.ModuleCategory;
import io.github.qe7.utils.local.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkProviderClient;
import net.minecraft.src.SaveHandler;
import net.minecraft.src.WorldClient;

import java.io.File;

public class WorldDLModule extends Module {

    public boolean isDownloading;
    public static WorldDLModule instance;
    private final Minecraft mc;

    public WorldDLModule() {
        super("World Downloader", "Downloads server's world to your computer", ModuleCategory.MISC);
        mc = Minecraft.getMinecraft();
        this.isDownloading = false;
        instance = this;
    }

    @Override
    public void onEnable() {
        this.isDownloading = false;
        if (mc.isMultiplayerWorld()) {
            this.startDownload();
        } else {
            this.setEnabled(false);
        }
    }

    @Override
    public void onDisable() {
        this.isDownloading = false;
        if (mc.isMultiplayerWorld()) {
            stopDownload();
        }
    }

    public void stopDownload() {
        WorldClient worldclient = (WorldClient) mc.theWorld;
        worldclient.saveWorld(true, null);
        worldclient.worldInfo.setWorldName(mc.gameSettings.lastServer + " " + System.currentTimeMillis());
        worldclient.downloadThisWorld = false;
        worldclient.downloadChunkLoader = null;
        worldclient.downloadSaveHandler = null;

        this.isDownloading = false;

        ChatUtil.addPrefixedMessage("World Downloader", "§cDownload stopped.");
    }

    public void startDownload() {
        this.isDownloading = true;
        String s = mc.gameSettings.lastServer;

        if (s.isEmpty()) {
            s = "Downloaded World";
        }

        if (s.contains(":"))
            s = s.replace(':', '_');

        WorldClient worldclient = (WorldClient) mc.theWorld;
        worldclient.worldInfo.setWorldName(s);
        worldclient.downloadSaveHandler = (SaveHandler) mc.getSaveLoader().getSaveLoader(s, false);
        worldclient.downloadChunkLoader = worldclient.downloadSaveHandler.getChunkLoader(worldclient.worldProvider);
        worldclient.worldInfo.setSizeOnDisk(getFileSizeRecursive(worldclient.downloadSaveHandler.getSaveDirectory()));

        Chunk.worldClient = worldclient;

        ((ChunkProviderClient) worldclient.chunkProvider).importOldTileEntities();

        worldclient.downloadThisWorld = true;

        ChatUtil.addPrefixedMessage("World Downloader", "§cDownloading everything you can see...");
        ChatUtil.addPrefixedMessage("World Downloader", "§6You can increase that area by travelling around.");
    }

    private long getFileSizeRecursive(File file) {
        long l = 0;
        File[] afile = file.listFiles();

        if (afile == null) {
            return 0;
        }

        for (File file1 : afile) {
            if (file1.isDirectory()) {
                l += getFileSizeRecursive(file1);
            } else if (file1.isFile()) {
                l += file1.length();
            }
        }
        return l;
    }
}
