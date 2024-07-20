package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.Bidi;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

public class FontRenderer
{
    private static final Pattern field_52015_r = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
    private int charWidth[];
    public int fontTextureName;

    /** the height in pixels of default text */
    public int FONT_HEIGHT;
    public Random fontRandom;
    private byte glyphWidth[];
    private final int glyphTextureName[];
    private int colorCode[];

    /**
     * The currently bound GL texture ID. Avoids unnecessary glBindTexture() for the same texture if it's already bound.
     */
    private int boundTextureName;

    /** The RenderEngine used to load and setup glyph textures. */
    private final RenderEngine renderEngine;

    /** Current X coordinate at which to draw the next character. */
    private float posX;

    /** Current Y coordinate at which to draw the next character. */
    private float posY;

    /**
     * If true, strings should be rendered with Unicode fonts instead of the default.png font
     */
    private boolean unicodeFlag;

    /**
     * If true, the Unicode Bidirectional Algorithm should be run before rendering any string.
     */
    private boolean bidiFlag;
    private float field_50115_n;
    private float field_50116_o;
    private float field_50118_p;
    private float field_50117_q;

    FontRenderer()
    {
        charWidth = new int[256];
        fontTextureName = 0;
        FONT_HEIGHT = 8;
        fontRandom = new Random();
        glyphWidth = new byte[0x10000];
        glyphTextureName = new int[256];
        colorCode = new int[32];
        renderEngine = null;
    }

    public FontRenderer(GameSettings par1GameSettings, String par2Str, RenderEngine par3RenderEngine, boolean par4)
    {
        charWidth = new int[256];
        fontTextureName = 0;
        FONT_HEIGHT = 8;
        fontRandom = new Random();
        glyphWidth = new byte[0x10000];
        glyphTextureName = new int[256];
        colorCode = new int[32];
        renderEngine = par3RenderEngine;
        unicodeFlag = par4;
        BufferedImage bufferedimage;

        try
        {
            bufferedimage = ImageIO.read((RenderEngine.class).getResourceAsStream(par2Str));
            InputStream inputstream = (RenderEngine.class).getResourceAsStream("/font/glyph_sizes.bin");
            inputstream.read(glyphWidth);
        }
        catch (IOException ioexception)
        {
            throw new RuntimeException(ioexception);
        }

        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        int ai[] = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, ai, 0, i);

        for (int k = 0; k < 256; k++)
        {
            int i1 = k % 16;
            int k1 = k / 16;
            int i2 = 7;

            do
            {
                if (i2 < 0)
                {
                    break;
                }

                int k2 = i1 * 8 + i2;
                boolean flag = true;

                for (int j3 = 0; j3 < 8 && flag; j3++)
                {
                    int l3 = (k1 * 8 + j3) * i;
                    int j4 = ai[k2 + l3] & 0xff;

                    if (j4 > 0)
                    {
                        flag = false;
                    }
                }

                if (!flag)
                {
                    break;
                }

                i2--;
            }
            while (true);

            if (k == 32)
            {
                i2 = 2;
            }

            charWidth[k] = i2 + 2;
        }

        fontTextureName = par3RenderEngine.allocateAndSetupTexture(bufferedimage);

        for (int l = 0; l < 32; l++)
        {
            int j1 = (l >> 3 & 1) * 85;
            int l1 = (l >> 2 & 1) * 170 + j1;
            int j2 = (l >> 1 & 1) * 170 + j1;
            int l2 = (l >> 0 & 1) * 170 + j1;

            if (l == 6)
            {
                l1 += 85;
            }

            if (par1GameSettings.anaglyph)
            {
                int i3 = (l1 * 30 + j2 * 59 + l2 * 11) / 100;
                int k3 = (l1 * 30 + j2 * 70) / 100;
                int i4 = (l1 * 30 + l2 * 70) / 100;
                l1 = i3;
                j2 = k3;
                l2 = i4;
            }

            if (l >= 16)
            {
                l1 /= 4;
                j2 /= 4;
                l2 /= 4;
            }

            colorCode[l] = (l1 & 0xff) << 16 | (j2 & 0xff) << 8 | l2 & 0xff;
        }
    }

    private float func_50112_a(int par1, char par2, boolean par3)
    {
        if (par2 == ' ')
        {
            return 4F;
        }

        if (par1 > 0 && !unicodeFlag)
        {
            return func_50106_a(par1 + 32, par3);
        }
        else
        {
            return func_50111_a(par2, par3);
        }
    }

    private float func_50106_a(int par1, boolean par2)
    {
        float f = (par1 % 16) * 8;
        float f1 = (par1 / 16) * 8;
        float f2 = par2 ? 1.0F : 0.0F;

        if (boundTextureName != fontTextureName)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTextureName);
            boundTextureName = fontTextureName;
        }

        float f3 = (float)charWidth[par1] - 0.01F;
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(f / 128F, f1 / 128F);
        GL11.glVertex3f(posX + f2, posY, 0.0F);
        GL11.glTexCoord2f(f / 128F, (f1 + 7.99F) / 128F);
        GL11.glVertex3f(posX - f2, posY + 7.99F, 0.0F);
        GL11.glTexCoord2f((f + f3) / 128F, f1 / 128F);
        GL11.glVertex3f(posX + f3 + f2, posY, 0.0F);
        GL11.glTexCoord2f((f + f3) / 128F, (f1 + 7.99F) / 128F);
        GL11.glVertex3f((posX + f3) - f2, posY + 7.99F, 0.0F);
        GL11.glEnd();
        return (float)charWidth[par1];
    }

    /**
     * Load one of the /font/glyph_XX.png into a new GL texture and store the texture ID in glyphTextureName array.
     */
    private void loadGlyphTexture(int par1)
    {
        String s = String.format("/font/glyph_%02X.png", new Object[]
                {
                    Integer.valueOf(par1)
                });
        BufferedImage bufferedimage;

        try
        {
            bufferedimage = ImageIO.read((RenderEngine.class).getResourceAsStream(s));
        }
        catch (IOException ioexception)
        {
            throw new RuntimeException(ioexception);
        }

        glyphTextureName[par1] = renderEngine.allocateAndSetupTexture(bufferedimage);
        boundTextureName = glyphTextureName[par1];
    }

    private float func_50111_a(char par1, boolean par2)
    {
        if (glyphWidth[par1] == 0)
        {
            return 0.0F;
        }

        int i = par1 / 256;

        if (glyphTextureName[i] == 0)
        {
            loadGlyphTexture(i);
        }

        if (boundTextureName != glyphTextureName[i])
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, glyphTextureName[i]);
            boundTextureName = glyphTextureName[i];
        }

        int j = glyphWidth[par1] >>> 4;
        int k = glyphWidth[par1] & 0xf;
        float f = j;
        float f1 = k + 1;
        float f2 = (float)((par1 % 16) * 16) + f;
        float f3 = ((par1 & 0xff) / 16) * 16;
        float f4 = f1 - f - 0.02F;
        float f5 = par2 ? 1.0F : 0.0F;
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(f2 / 256F, f3 / 256F);
        GL11.glVertex3f(posX + f5, posY, 0.0F);
        GL11.glTexCoord2f(f2 / 256F, (f3 + 15.98F) / 256F);
        GL11.glVertex3f(posX - f5, posY + 7.99F, 0.0F);
        GL11.glTexCoord2f((f2 + f4) / 256F, f3 / 256F);
        GL11.glVertex3f(posX + f4 / 2.0F + f5, posY, 0.0F);
        GL11.glTexCoord2f((f2 + f4) / 256F, (f3 + 15.98F) / 256F);
        GL11.glVertex3f((posX + f4 / 2.0F) - f5, posY + 7.99F, 0.0F);
        GL11.glEnd();
        return (f1 - f) / 2.0F + 1.0F;
    }

    /**
     * Draws the specified string with a shadow.
     */
    public int drawStringWithShadow(String par1Str, float par2, float par3, int par4)
    {
        if (bidiFlag)
        {
            par1Str = bidiReorder(par1Str);
        }

        int i = func_50101_a(par1Str, par2 + 1, par3 + 1, par4, true);
        i = Math.max(i, func_50101_a(par1Str, par2, par3, par4, false));
        return i;
    }

    /**
     * Draws the specified string.
     */
    public void drawString(String par1Str, float par2, float par3, int par4)
    {
        if (bidiFlag)
        {
            par1Str = bidiReorder(par1Str);
        }

        func_50101_a(par1Str, par2, par3, par4, false);
    }

    /**
     * Apply Unicode Bidirectional Algorithm to string and return a new possibly reordered string for visual rendering.
     */
    private String bidiReorder(String par1Str)
    {
        if (par1Str == null || !Bidi.requiresBidi(par1Str.toCharArray(), 0, par1Str.length()))
        {
            return par1Str;
        }

        Bidi bidi = new Bidi(par1Str, -2);
        byte abyte0[] = new byte[bidi.getRunCount()];
        String as[] = new String[abyte0.length];

        for (int i = 0; i < abyte0.length; i++)
        {
            int j = bidi.getRunStart(i);
            int k = bidi.getRunLimit(i);
            int i1 = bidi.getRunLevel(i);
            String s = par1Str.substring(j, k);
            abyte0[i] = (byte)i1;
            as[i] = s;
        }

        String as1[] = (String[])as.clone();
        Bidi.reorderVisually(abyte0, 0, as, 0, abyte0.length);
        StringBuilder stringbuilder = new StringBuilder();
        label0:

        for (int l = 0; l < as.length; l++)
        {
            byte byte0 = abyte0[l];
            int j1 = 0;

            do
            {
                if (j1 >= as1.length)
                {
                    break;
                }

                if (as1[j1].equals(as[l]))
                {
                    byte0 = abyte0[j1];
                    break;
                }

                j1++;
            }
            while (true);

            if ((byte0 & 1) == 0)
            {
                stringbuilder.append(as[l]);
                continue;
            }

            j1 = as[l].length() - 1;

            do
            {
                if (j1 < 0)
                {
                    continue label0;
                }

                char c = as[l].charAt(j1);

                if (c == '(')
                {
                    c = ')';
                }
                else if (c == ')')
                {
                    c = '(';
                }

                stringbuilder.append(c);
                j1--;
            }
            while (true);
        }

        return stringbuilder.toString();
    }

    /**
     * Render a single line string at the current (posX,posY) and update posX
     */
    private void renderStringAtPos(String par1Str, boolean par2)
    {
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;

        for (int i = 0; i < par1Str.length(); i++)
        {
            char c = par1Str.charAt(i);

            if (c == '\247' && i + 1 < par1Str.length())
            {
                int j = "0123456789abcdefklmnor".indexOf(par1Str.toLowerCase().charAt(i + 1));

                if (j < 16)
                {
                    flag = false;
                    flag1 = false;
                    flag4 = false;
                    flag3 = false;
                    flag2 = false;

                    if (j < 0 || j > 15)
                    {
                        j = 15;
                    }

                    if (par2)
                    {
                        j += 16;
                    }

                    int l = colorCode[j];
                    GL11.glColor3f((float)(l >> 16) / 255F, (float)(l >> 8 & 0xff) / 255F, (float)(l & 0xff) / 255F);
                }
                else if (j == 16)
                {
                    flag = true;
                }
                else if (j == 17)
                {
                    flag1 = true;
                }
                else if (j == 18)
                {
                    flag4 = true;
                }
                else if (j == 19)
                {
                    flag3 = true;
                }
                else if (j == 20)
                {
                    flag2 = true;
                }
                else if (j == 21)
                {
                    flag = false;
                    flag1 = false;
                    flag4 = false;
                    flag3 = false;
                    flag2 = false;
                    GL11.glColor4f(field_50115_n, field_50116_o, field_50118_p, field_50117_q);
                }

                i++;
                continue;
            }

            int k = ChatAllowedCharacters.allowedCharacters.indexOf(c);

            if (flag && k > 0)
            {
                int i1;

                do
                {
                    i1 = fontRandom.nextInt(ChatAllowedCharacters.allowedCharacters.length());
                }
                while (charWidth[k + 32] != charWidth[i1 + 32]);

                k = i1;
            }

            float f = func_50112_a(k, c, flag2);

            if (flag1)
            {
                posX++;
                func_50112_a(k, c, flag2);
                posX--;
                f++;
            }

            if (flag4)
            {
                Tessellator tessellator = Tessellator.instance;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                tessellator.startDrawingQuads();
                tessellator.addVertex(posX, posY + (float)(FONT_HEIGHT / 2), 0.0D);
                tessellator.addVertex(posX + f, posY + (float)(FONT_HEIGHT / 2), 0.0D);
                tessellator.addVertex(posX + f, (posY + (float)(FONT_HEIGHT / 2)) - 1.0F, 0.0D);
                tessellator.addVertex(posX, (posY + (float)(FONT_HEIGHT / 2)) - 1.0F, 0.0D);
                tessellator.draw();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            if (flag3)
            {
                Tessellator tessellator1 = Tessellator.instance;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                tessellator1.startDrawingQuads();
                int j1 = flag3 ? -1 : 0;
                tessellator1.addVertex(posX + (float)j1, posY + (float)FONT_HEIGHT, 0.0D);
                tessellator1.addVertex(posX + f, posY + (float)FONT_HEIGHT, 0.0D);
                tessellator1.addVertex(posX + f, (posY + (float)FONT_HEIGHT) - 1.0F, 0.0D);
                tessellator1.addVertex(posX + (float)j1, (posY + (float)FONT_HEIGHT) - 1.0F, 0.0D);
                tessellator1.draw();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            posX += f;
        }
    }

    public int func_50101_a(String par1Str, float par2, float par3, int par4, boolean par5)
    {
        if (par1Str != null)
        {
            boundTextureName = 0;

            if ((par4 & 0xfc000000) == 0)
            {
                par4 |= 0xff000000;
            }

            if (par5)
            {
                par4 = (par4 & 0xfcfcfc) >> 2 | par4 & 0xff000000;
            }

            field_50115_n = (float)(par4 >> 16 & 0xff) / 255F;
            field_50116_o = (float)(par4 >> 8 & 0xff) / 255F;
            field_50118_p = (float)(par4 & 0xff) / 255F;
            field_50117_q = (float)(par4 >> 24 & 0xff) / 255F;
            GL11.glColor4f(field_50115_n, field_50116_o, field_50118_p, field_50117_q);
            posX = par2;
            posY = par3;
            renderStringAtPos(par1Str, par5);
            return (int)posX;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Returns the width of this string. Equivalent of FontMetrics.stringWidth(String s).
     */
    public int getStringWidth(String par1Str)
    {
        if (par1Str == null)
        {
            return 0;
        }

        int i = 0;
        boolean flag = false;

        for (int j = 0; j < par1Str.length(); j++)
        {
            char c = par1Str.charAt(j);
            int k = func_50105_a(c);

            if (k < 0 && j < par1Str.length() - 1)
            {
                char c1 = par1Str.charAt(++j);

                if (c1 == 'l' || c1 == 'L')
                {
                    flag = true;
                }
                else if (c1 == 'r' || c1 == 'R')
                {
                    flag = false;
                }

                k = func_50105_a(c1);
            }

            i += k;

            if (flag)
            {
                i++;
            }
        }

        return i;
    }

    public int func_50105_a(char par1)
    {
        if (par1 == '\247')
        {
            return -1;
        }

        int i = ChatAllowedCharacters.allowedCharacters.indexOf(par1);

        if (i >= 0 && !unicodeFlag)
        {
            return charWidth[i + 32];
        }

        if (glyphWidth[par1] != 0)
        {
            int j = glyphWidth[par1] >> 4;
            int k = glyphWidth[par1] & 0xf;

            if (k > 7)
            {
                k = 15;
                j = 0;
            }

            return (++k - j) / 2 + 1;
        }
        else
        {
            return 0;
        }
    }

    public String func_50107_a(String par1Str, int par2)
    {
        return func_50104_a(par1Str, par2, false);
    }

    public String func_50104_a(String par1Str, int par2, boolean par3)
    {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = par3 ? par1Str.length() - 1 : 0;
        byte byte0 = ((byte)(par3 ? -1 : 1));
        boolean flag = false;
        boolean flag1 = false;

        for (int k = j; k >= 0 && k < par1Str.length() && i < par2; k += byte0)
        {
            char c = par1Str.charAt(k);
            int l = func_50105_a(c);

            if (flag)
            {
                flag = false;

                if (c == 'l' || c == 'L')
                {
                    flag1 = true;
                }
                else if (c == 'r' || c == 'R')
                {
                    flag1 = false;
                }
            }
            else if (l < 0)
            {
                flag = true;
            }
            else
            {
                i += l;

                if (flag1)
                {
                    i++;
                }
            }

            if (i > par2)
            {
                break;
            }

            if (par3)
            {
                stringbuilder.insert(0, c);
            }
            else
            {
                stringbuilder.append(c);
            }
        }

        return stringbuilder.toString();
    }

    /**
     * Splits and draws a String with wordwrap (maximum length is parameter k)
     */
    public void drawSplitString(String par1Str, int par2, int par3, int par4, int par5)
    {
        if (bidiFlag)
        {
            par1Str = bidiReorder(par1Str);
        }

        renderSplitStringNoShadow(par1Str, par2, par3, par4, par5);
    }

    /**
     * renders a multi-line string with wordwrap (maximum length is parameter k) by means of renderSplitString
     */
    private void renderSplitStringNoShadow(String par1Str, int par2, int par3, int par4, int par5)
    {
        renderSplitString(par1Str, par2, par3, par4, par5, false);
    }

    /**
     * Perform actual work of rendering a multi-line string with wordwrap (maximum length is parameter k) and with
     * darkre drop shadow color if flag is set
     */
    private void renderSplitString(String par1Str, int par2, int par3, int par4, int par5, boolean par6)
    {
        String as[] = par1Str.split("\n");

        if (as.length > 1)
        {
            for (int i = 0; i < as.length; i++)
            {
                renderSplitStringNoShadow(as[i], par2, par3, par4, par5);
                par3 += splitStringWidth(as[i], par4);
            }

            return;
        }

        String as1[] = par1Str.split(" ");
        int j = 0;
        String s = "";

        do
        {
            if (j >= as1.length)
            {
                break;
            }

            String s1;

            for (s1 = (new StringBuilder()).append(s).append(as1[j++]).append(" ").toString(); j < as1.length && getStringWidth((new StringBuilder()).append(s1).append(as1[j]).toString()) < par4; s1 = (new StringBuilder()).append(s1).append(as1[j++]).append(" ").toString()) { }

            int k;

            for (; getStringWidth(s1) > par4; s1 = (new StringBuilder()).append(s).append(s1.substring(k)).toString())
            {
                for (k = 0; getStringWidth(s1.substring(0, k + 1)) <= par4; k++) { }

                if (s1.substring(0, k).trim().length() <= 0)
                {
                    continue;
                }

                String s2 = s1.substring(0, k);

                if (s2.lastIndexOf("\247") >= 0)
                {
                    s = (new StringBuilder()).append("\247").append(s2.charAt(s2.lastIndexOf("\247") + 1)).toString();
                }

                func_50101_a(s2, par2, par3, par5, par6);
                par3 += FONT_HEIGHT;
            }

            if (getStringWidth(s1.trim()) > 0)
            {
                if (s1.lastIndexOf("\247") >= 0)
                {
                    s = (new StringBuilder()).append("\247").append(s1.charAt(s1.lastIndexOf("\247") + 1)).toString();
                }

                func_50101_a(s1, par2, par3, par5, par6);
                par3 += FONT_HEIGHT;
            }
        }
        while (true);
    }

    /**
     * Returns the width of the wordwrapped String (maximum length is parameter k)
     */
    public int splitStringWidth(String par1Str, int par2)
    {
        String as[] = par1Str.split("\n");

        if (as.length > 1)
        {
            int i = 0;

            for (int j = 0; j < as.length; j++)
            {
                i += splitStringWidth(as[j], par2);
            }

            return i;
        }

        String as1[] = par1Str.split(" ");
        int k = 0;
        int l = 0;

        do
        {
            if (k >= as1.length)
            {
                break;
            }

            String s;

            for (s = (new StringBuilder()).append(as1[k++]).append(" ").toString(); k < as1.length && getStringWidth((new StringBuilder()).append(s).append(as1[k]).toString()) < par2; s = (new StringBuilder()).append(s).append(as1[k++]).append(" ").toString()) { }

            int i1;

            for (; getStringWidth(s) > par2; s = s.substring(i1))
            {
                for (i1 = 0; getStringWidth(s.substring(0, i1 + 1)) <= par2; i1++) { }

                if (s.substring(0, i1).trim().length() > 0)
                {
                    l += FONT_HEIGHT;
                }
            }

            if (s.trim().length() > 0)
            {
                l += FONT_HEIGHT;
            }
        }
        while (true);

        if (l < FONT_HEIGHT)
        {
            l += FONT_HEIGHT;
        }

        return l;
    }

    /**
     * Set unicodeFlag controlling whether strings should be rendered with Unicode fonts instead of the default.png
     * font.
     */
    public void setUnicodeFlag(boolean par1)
    {
        unicodeFlag = par1;
    }

    /**
     * Set bidiFlag to control if the Unicode Bidirectional Algorithm should be run before rendering any string.
     */
    public void setBidiFlag(boolean par1)
    {
        bidiFlag = par1;
    }

    public java.util.List func_50108_c(String par1Str, int par2)
    {
        return Arrays.asList(func_50113_d(par1Str, par2).split("\n"));
    }

    String func_50113_d(String par1Str, int par2)
    {
        int i = func_50102_e(par1Str, par2);

        if (par1Str.length() <= i)
        {
            return par1Str;
        }
        else
        {
            String s = par1Str.substring(0, i);
            String s1 = (new StringBuilder()).append(func_50114_c(s)).append(par1Str.substring(i + (par1Str.charAt(i) != ' ' ? 0 : 1))).toString();
            return (new StringBuilder()).append(s).append("\n").append(func_50113_d(s1, par2)).toString();
        }
    }

    private int func_50102_e(String par1Str, int par2)
    {
        int i = par1Str.length();
        int j = 0;
        int k = 0;
        int l = -1;
        boolean flag = false;

        do
        {
            if (k >= i)
            {
                break;
            }

            char c = par1Str.charAt(k);

            switch (c)
            {
                case 167:
                    if (k != i)
                    {
                        char c1 = par1Str.charAt(++k);

                        if (c1 == 'l' || c1 == 'L')
                        {
                            flag = true;
                        }
                        else if (c1 == 'r' || c1 == 'R')
                        {
                            flag = false;
                        }
                    }

                    break;

                case 32:
                    l = k;

                default:
                    j += func_50105_a(c);

                    if (flag)
                    {
                        j++;
                    }

                    break;
            }

            if (c == '\n')
            {
                l = ++k;
                break;
            }

            if (j > par2)
            {
                break;
            }

            k++;
        }
        while (true);

        if (k != i && l != -1 && l < k)
        {
            return l;
        }
        else
        {
            return k;
        }
    }

    private static boolean func_50110_b(char par0)
    {
        return par0 >= '0' && par0 <= '9' || par0 >= 'a' && par0 <= 'f' || par0 >= 'A' && par0 <= 'F';
    }

    private static boolean func_50109_c(char par0)
    {
        return par0 >= 'k' && par0 <= 'o' || par0 >= 'K' && par0 <= 'O' || par0 == 'r' || par0 == 'R';
    }

    private static String func_50114_c(String par0Str)
    {
        String s = "";
        int i = -1;
        int j = par0Str.length();

        do
        {
            if ((i = par0Str.indexOf('\247', i + 1)) == -1)
            {
                break;
            }

            if (i < j - 1)
            {
                char c = par0Str.charAt(i + 1);

                if (func_50110_b(c))
                {
                    s = (new StringBuilder()).append("\247").append(c).toString();
                }
                else if (func_50109_c(c))
                {
                    s = (new StringBuilder()).append(s).append("\247").append(c).toString();
                }
            }
        }
        while (true);

        return s;
    }

    public static String func_52014_d(String par0Str)
    {
        return field_52015_r.matcher(par0Str).replaceAll("");
    }
}
