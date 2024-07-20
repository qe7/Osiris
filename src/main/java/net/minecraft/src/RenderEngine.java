package net.minecraft.src;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

public class RenderEngine
{
    /** Use mipmaps for all bound textures (unused at present) */
    public static boolean useMipmaps = false;
    private HashMap textureMap;

    /** Texture contents map (key: texture name, value: int[] contents) */
    private HashMap textureContentsMap;

    /** A mapping from GL texture names (integers) to BufferedImage instances */
    private IntHashMap textureNameToImageMap;

    /** An IntBuffer storing 1 int used as scratch space in RenderEngine */
    private IntBuffer singleIntBuffer;

    /** Stores the image data for the texture. */
    private ByteBuffer imageData;
    private java.util.List textureList;

    /** A mapping from image URLs to ThreadDownloadImageData instances */
    private Map urlToImageDataMap;

    /** Reference to the GameSettings object */
    private GameSettings options;

    /** Flag set when a texture should not be repeated */
    public boolean clampTexture;

    /** Flag set when a texture should use blurry resizing */
    public boolean blurTexture;

    /** Texture pack */
    private TexturePackList texturePack;

    /** Missing texture image */
    private BufferedImage missingTextureImage;
    private int field_48512_n;

    public RenderEngine(TexturePackList par1TexturePackList, GameSettings par2GameSettings)
    {
        textureMap = new HashMap();
        textureContentsMap = new HashMap();
        textureNameToImageMap = new IntHashMap();
        singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
        imageData = GLAllocation.createDirectByteBuffer(0x1000000);
        textureList = new ArrayList();
        urlToImageDataMap = new HashMap();
        clampTexture = false;
        blurTexture = false;
        missingTextureImage = new BufferedImage(64, 64, 2);
        field_48512_n = 16;
        texturePack = par1TexturePackList;
        options = par2GameSettings;
        Graphics g = missingTextureImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 64, 64);
        g.setColor(Color.BLACK);
        g.drawString("missingtex", 1, 10);
        g.dispose();
    }

    public int[] getTextureContents(String par1Str)
    {
        TexturePackBase texturepackbase = texturePack.selectedTexturePack;
        int ai[] = (int[])textureContentsMap.get(par1Str);

        if (ai != null)
        {
            return ai;
        }

        try
        {
            int ai1[] = null;

            if (par1Str.startsWith("##"))
            {
                ai1 = getImageContentsAndAllocate(unwrapImageByColumns(readTextureImage(texturepackbase.getResourceAsStream(par1Str.substring(2)))));
            }
            else if (par1Str.startsWith("%clamp%"))
            {
                clampTexture = true;
                ai1 = getImageContentsAndAllocate(readTextureImage(texturepackbase.getResourceAsStream(par1Str.substring(7))));
                clampTexture = false;
            }
            else if (par1Str.startsWith("%blur%"))
            {
                blurTexture = true;
                clampTexture = true;
                ai1 = getImageContentsAndAllocate(readTextureImage(texturepackbase.getResourceAsStream(par1Str.substring(6))));
                clampTexture = false;
                blurTexture = false;
            }
            else
            {
                InputStream inputstream = texturepackbase.getResourceAsStream(par1Str);

                if (inputstream == null)
                {
                    ai1 = getImageContentsAndAllocate(missingTextureImage);
                }
                else
                {
                    ai1 = getImageContentsAndAllocate(readTextureImage(inputstream));
                }
            }

            textureContentsMap.put(par1Str, ai1);
            return ai1;
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }

        int ai2[] = getImageContentsAndAllocate(missingTextureImage);
        textureContentsMap.put(par1Str, ai2);
        return ai2;
    }

    private int[] getImageContentsAndAllocate(BufferedImage par1BufferedImage)
    {
        int i = par1BufferedImage.getWidth();
        int j = par1BufferedImage.getHeight();
        int ai[] = new int[i * j];
        par1BufferedImage.getRGB(0, 0, i, j, ai, 0, i);
        return ai;
    }

    private int[] getImageContents(BufferedImage par1BufferedImage, int par2ArrayOfInteger[])
    {
        int i = par1BufferedImage.getWidth();
        int j = par1BufferedImage.getHeight();
        par1BufferedImage.getRGB(0, 0, i, j, par2ArrayOfInteger, 0, i);
        return par2ArrayOfInteger;
    }

    public int getTexture(String par1Str)
    {
        TexturePackBase texturepackbase = texturePack.selectedTexturePack;
        Integer integer = (Integer)textureMap.get(par1Str);

        if (integer != null)
        {
            return integer.intValue();
        }

        try
        {
            singleIntBuffer.clear();
            GLAllocation.generateTextureNames(singleIntBuffer);
            int i = singleIntBuffer.get(0);

            if (par1Str.startsWith("##"))
            {
                setupTexture(unwrapImageByColumns(readTextureImage(texturepackbase.getResourceAsStream(par1Str.substring(2)))), i);
            }
            else if (par1Str.startsWith("%clamp%"))
            {
                clampTexture = true;
                setupTexture(readTextureImage(texturepackbase.getResourceAsStream(par1Str.substring(7))), i);
                clampTexture = false;
            }
            else if (par1Str.startsWith("%blur%"))
            {
                blurTexture = true;
                setupTexture(readTextureImage(texturepackbase.getResourceAsStream(par1Str.substring(6))), i);
                blurTexture = false;
            }
            else if (par1Str.startsWith("%blurclamp%"))
            {
                blurTexture = true;
                clampTexture = true;
                setupTexture(readTextureImage(texturepackbase.getResourceAsStream(par1Str.substring(11))), i);
                blurTexture = false;
                clampTexture = false;
            }
            else
            {
                InputStream inputstream = texturepackbase.getResourceAsStream(par1Str);

                if (inputstream == null)
                {
                    setupTexture(missingTextureImage, i);
                }
                else
                {
                    setupTexture(readTextureImage(inputstream), i);
                }
            }

            textureMap.put(par1Str, Integer.valueOf(i));
            return i;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        GLAllocation.generateTextureNames(singleIntBuffer);
        int j = singleIntBuffer.get(0);
        setupTexture(missingTextureImage, j);
        textureMap.put(par1Str, Integer.valueOf(j));
        return j;
    }

    /**
     * Takes an image with multiple 16-pixel-wide columns and creates a new 16-pixel-wide image where the columns are
     * stacked vertically
     */
    private BufferedImage unwrapImageByColumns(BufferedImage par1BufferedImage)
    {
        int i = par1BufferedImage.getWidth() / 16;
        BufferedImage bufferedimage = new BufferedImage(16, par1BufferedImage.getHeight() * i, 2);
        Graphics g = bufferedimage.getGraphics();

        for (int j = 0; j < i; j++)
        {
            g.drawImage(par1BufferedImage, -j * 16, j * par1BufferedImage.getHeight(), null);
        }

        g.dispose();
        return bufferedimage;
    }

    /**
     * Copy the supplied image onto a newly-allocated OpenGL texture, returning the allocated texture name
     */
    public int allocateAndSetupTexture(BufferedImage par1BufferedImage)
    {
        singleIntBuffer.clear();
        GLAllocation.generateTextureNames(singleIntBuffer);
        int i = singleIntBuffer.get(0);
        setupTexture(par1BufferedImage, i);
        textureNameToImageMap.addKey(i, par1BufferedImage);
        return i;
    }

    /**
     * Copy the supplied image onto the specified OpenGL texture
     */
    public void setupTexture(BufferedImage par1BufferedImage, int par2)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, par2);

        if (useMipmaps)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }
        else
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }

        if (blurTexture)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        }

        if (clampTexture)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        }
        else
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        }

        int i = par1BufferedImage.getWidth();
        int j = par1BufferedImage.getHeight();
        int ai[] = new int[i * j];
        byte abyte0[] = new byte[i * j * 4];
        par1BufferedImage.getRGB(0, 0, i, j, ai, 0, i);

        for (int k = 0; k < ai.length; k++)
        {
            int i1 = ai[k] >> 24 & 0xff;
            int k1 = ai[k] >> 16 & 0xff;
            int i2 = ai[k] >> 8 & 0xff;
            int k2 = ai[k] & 0xff;

            if (options != null && options.anaglyph)
            {
                int i3 = (k1 * 30 + i2 * 59 + k2 * 11) / 100;
                int k3 = (k1 * 30 + i2 * 70) / 100;
                int i4 = (k1 * 30 + k2 * 70) / 100;
                k1 = i3;
                i2 = k3;
                k2 = i4;
            }

            abyte0[k * 4 + 0] = (byte)k1;
            abyte0[k * 4 + 1] = (byte)i2;
            abyte0[k * 4 + 2] = (byte)k2;
            abyte0[k * 4 + 3] = (byte)i1;
        }

        imageData.clear();
        imageData.put(abyte0);
        imageData.position(0).limit(abyte0.length);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, i, j, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);

        if (useMipmaps)
        {
            for (int l = 1; l <= 4; l++)
            {
                int j1 = i >> l - 1;
                int l1 = i >> l;
                int j2 = j >> l;

                for (int l2 = 0; l2 < l1; l2++)
                {
                    for (int j3 = 0; j3 < j2; j3++)
                    {
                        int l3 = imageData.getInt((l2 * 2 + 0 + (j3 * 2 + 0) * j1) * 4);
                        int j4 = imageData.getInt((l2 * 2 + 1 + (j3 * 2 + 0) * j1) * 4);
                        int k4 = imageData.getInt((l2 * 2 + 1 + (j3 * 2 + 1) * j1) * 4);
                        int l4 = imageData.getInt((l2 * 2 + 0 + (j3 * 2 + 1) * j1) * 4);
                        int i5 = alphaBlend(alphaBlend(l3, j4), alphaBlend(k4, l4));
                        imageData.putInt((l2 + j3 * l1) * 4, i5);
                    }
                }

                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, l, GL11.GL_RGBA, l1, j2, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
            }
        }
    }

    public void createTextureFromBytes(int par1ArrayOfInteger[], int par2, int par3, int par4)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, par4);

        if (useMipmaps)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }
        else
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }

        if (blurTexture)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        }

        if (clampTexture)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        }
        else
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        }

        byte abyte0[] = new byte[par2 * par3 * 4];

        for (int i = 0; i < par1ArrayOfInteger.length; i++)
        {
            int j = par1ArrayOfInteger[i] >> 24 & 0xff;
            int k = par1ArrayOfInteger[i] >> 16 & 0xff;
            int l = par1ArrayOfInteger[i] >> 8 & 0xff;
            int i1 = par1ArrayOfInteger[i] & 0xff;

            if (options != null && options.anaglyph)
            {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }

            abyte0[i * 4 + 0] = (byte)k;
            abyte0[i * 4 + 1] = (byte)l;
            abyte0[i * 4 + 2] = (byte)i1;
            abyte0[i * 4 + 3] = (byte)j;
        }

        imageData.clear();
        imageData.put(abyte0);
        imageData.position(0).limit(abyte0.length);
        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, par2, par3, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
    }

    /**
     * Deletes a single GL texture
     */
    public void deleteTexture(int par1)
    {
        textureNameToImageMap.removeObject(par1);
        singleIntBuffer.clear();
        singleIntBuffer.put(par1);
        singleIntBuffer.flip();
        GL11.glDeleteTextures(singleIntBuffer);
    }

    /**
     * Takes a URL of a downloadable image and the name of the local image to be used as a fallback.  If the image has
     * been downloaded, returns the GL texture of the downloaded image, otherwise returns the GL texture of the fallback
     * image.
     */
    public int getTextureForDownloadableImage(String par1Str, String par2Str)
    {
        ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)urlToImageDataMap.get(par1Str);

        if (threaddownloadimagedata != null && threaddownloadimagedata.image != null && !threaddownloadimagedata.textureSetupComplete)
        {
            if (threaddownloadimagedata.textureName < 0)
            {
                threaddownloadimagedata.textureName = allocateAndSetupTexture(threaddownloadimagedata.image);
            }
            else
            {
                setupTexture(threaddownloadimagedata.image, threaddownloadimagedata.textureName);
            }

            threaddownloadimagedata.textureSetupComplete = true;
        }

        if (threaddownloadimagedata == null || threaddownloadimagedata.textureName < 0)
        {
            if (par2Str == null)
            {
                return -1;
            }
            else
            {
                return getTexture(par2Str);
            }
        }
        else
        {
            return threaddownloadimagedata.textureName;
        }
    }

    /**
     * Return a ThreadDownloadImageData instance for the given URL.  If it does not already exist, it is created and
     * uses the passed ImageBuffer.  If it does, its reference count is incremented.
     */
    public ThreadDownloadImageData obtainImageData(String par1Str, ImageBuffer par2ImageBuffer)
    {
        ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)urlToImageDataMap.get(par1Str);

        if (threaddownloadimagedata == null)
        {
            urlToImageDataMap.put(par1Str, new ThreadDownloadImageData(par1Str, par2ImageBuffer));
        }
        else
        {
            threaddownloadimagedata.referenceCount++;
        }

        return threaddownloadimagedata;
    }

    /**
     * Decrements the reference count for a given URL, deleting the image data if the reference count hits 0
     */
    public void releaseImageData(String par1Str)
    {
        ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)urlToImageDataMap.get(par1Str);

        if (threaddownloadimagedata != null)
        {
            threaddownloadimagedata.referenceCount--;

            if (threaddownloadimagedata.referenceCount == 0)
            {
                if (threaddownloadimagedata.textureName >= 0)
                {
                    deleteTexture(threaddownloadimagedata.textureName);
                }

                urlToImageDataMap.remove(par1Str);
            }
        }
    }

    public void registerTextureFX(TextureFX par1TextureFX)
    {
        textureList.add(par1TextureFX);
        par1TextureFX.onTick();
    }

    public void updateDynamicTextures()
    {
        int i = -1;

        for (int j = 0; j < textureList.size(); j++)
        {
            TextureFX texturefx = (TextureFX)textureList.get(j);
            texturefx.anaglyphEnabled = options.anaglyph;
            texturefx.onTick();
            imageData.clear();
            imageData.put(texturefx.imageData);
            imageData.position(0).limit(texturefx.imageData.length);

            if (texturefx.iconIndex != i)
            {
                texturefx.bindImage(this);
                i = texturefx.iconIndex;
            }

            for (int k = 0; k < texturefx.tileSize; k++)
            {
                for (int l = 0; l < texturefx.tileSize; l++)
                {
                    GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, (texturefx.iconIndex % 16) * 16 + k * 16, (texturefx.iconIndex / 16) * 16 + l * 16, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
                }
            }
        }
    }

    /**
     * Uses the alpha of the two colors passed in to determine the contributions of each color.  If either of them has
     * an alpha greater than 0 then the returned alpha is 255 otherwise its zero if they are both zero. Args: color1,
     * color2
     */
    private int alphaBlend(int par1, int par2)
    {
        int i = (par1 & 0xff000000) >> 24 & 0xff;
        int j = (par2 & 0xff000000) >> 24 & 0xff;
        char c = '\377';

        if (i + j < 255)
        {
            c = '\0';
            i = 1;
            j = 1;
        }
        else if (i > j)
        {
            i = 255;
            j = 1;
        }
        else
        {
            i = 1;
            j = 255;
        }

        int k = (par1 >> 16 & 0xff) * i;
        int l = (par1 >> 8 & 0xff) * i;
        int i1 = (par1 & 0xff) * i;
        int j1 = (par2 >> 16 & 0xff) * j;
        int k1 = (par2 >> 8 & 0xff) * j;
        int l1 = (par2 & 0xff) * j;
        int i2 = (k + j1) / (i + j);
        int j2 = (l + k1) / (i + j);
        int k2 = (i1 + l1) / (i + j);
        return c << 24 | i2 << 16 | j2 << 8 | k2;
    }

    /**
     * Call setupTexture on all currently-loaded textures again to account for changes in rendering options
     */
    public void refreshTextures()
    {
        TexturePackBase texturepackbase = texturePack.selectedTexturePack;
        int i;
        BufferedImage bufferedimage;

        for (Iterator iterator = textureNameToImageMap.getKeySet().iterator(); iterator.hasNext(); setupTexture(bufferedimage, i))
        {
            i = ((Integer)iterator.next()).intValue();
            bufferedimage = (BufferedImage)textureNameToImageMap.lookup(i);
        }

        for (Iterator iterator1 = urlToImageDataMap.values().iterator(); iterator1.hasNext();)
        {
            ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)iterator1.next();
            threaddownloadimagedata.textureSetupComplete = false;
        }

        for (Iterator iterator2 = textureMap.keySet().iterator(); iterator2.hasNext();)
        {
            String s = (String)iterator2.next();

            try
            {
                BufferedImage bufferedimage1;

                if (s.startsWith("##"))
                {
                    bufferedimage1 = unwrapImageByColumns(readTextureImage(texturepackbase.getResourceAsStream(s.substring(2))));
                }
                else if (s.startsWith("%clamp%"))
                {
                    clampTexture = true;
                    bufferedimage1 = readTextureImage(texturepackbase.getResourceAsStream(s.substring(7)));
                }
                else if (s.startsWith("%blur%"))
                {
                    blurTexture = true;
                    bufferedimage1 = readTextureImage(texturepackbase.getResourceAsStream(s.substring(6)));
                }
                else if (s.startsWith("%blurclamp%"))
                {
                    blurTexture = true;
                    clampTexture = true;
                    bufferedimage1 = readTextureImage(texturepackbase.getResourceAsStream(s.substring(11)));
                }
                else
                {
                    bufferedimage1 = readTextureImage(texturepackbase.getResourceAsStream(s));
                }

                int j = ((Integer)textureMap.get(s)).intValue();
                setupTexture(bufferedimage1, j);
                blurTexture = false;
                clampTexture = false;
            }
            catch (IOException ioexception)
            {
                ioexception.printStackTrace();
            }
        }

        for (Iterator iterator3 = textureContentsMap.keySet().iterator(); iterator3.hasNext();)
        {
            String s1 = (String)iterator3.next();

            try
            {
                BufferedImage bufferedimage2;

                if (s1.startsWith("##"))
                {
                    bufferedimage2 = unwrapImageByColumns(readTextureImage(texturepackbase.getResourceAsStream(s1.substring(2))));
                }
                else if (s1.startsWith("%clamp%"))
                {
                    clampTexture = true;
                    bufferedimage2 = readTextureImage(texturepackbase.getResourceAsStream(s1.substring(7)));
                }
                else if (s1.startsWith("%blur%"))
                {
                    blurTexture = true;
                    bufferedimage2 = readTextureImage(texturepackbase.getResourceAsStream(s1.substring(6)));
                }
                else
                {
                    bufferedimage2 = readTextureImage(texturepackbase.getResourceAsStream(s1));
                }

                getImageContents(bufferedimage2, (int[])textureContentsMap.get(s1));
                blurTexture = false;
                clampTexture = false;
            }
            catch (IOException ioexception1)
            {
                ioexception1.printStackTrace();
            }
        }
    }

    /**
     * Returns a BufferedImage read off the provided input stream.  Args: inputStream
     */
    private BufferedImage readTextureImage(InputStream par1InputStream) throws IOException
    {
        BufferedImage bufferedimage = ImageIO.read(par1InputStream);
        par1InputStream.close();
        return bufferedimage;
    }

    public void bindTexture(int par1)
    {
        if (par1 < 0)
        {
            return;
        }
        else
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1);
            return;
        }
    }
}
