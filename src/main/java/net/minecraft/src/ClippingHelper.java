package net.minecraft.src;

public class ClippingHelper
{
    public float frustum[][];
    public float projectionMatrix[];
    public float modelviewMatrix[];
    public float clippingMatrix[];

    public ClippingHelper()
    {
        frustum = new float[16][16];
        projectionMatrix = new float[16];
        modelviewMatrix = new float[16];
        clippingMatrix = new float[16];
    }

    /**
     * Returns true if the box is inside all 6 clipping planes, otherwise returns false.
     */
    public boolean isBoxInFrustum(double par1, double par3, double par5, double par7, double par9, double par11)
    {
        for (int i = 0; i < 6; i++)
        {
            if ((double)frustum[i][0] * par1 + (double)frustum[i][1] * par3 + (double)frustum[i][2] * par5 + (double)frustum[i][3] <= 0.0D && (double)frustum[i][0] * par7 + (double)frustum[i][1] * par3 + (double)frustum[i][2] * par5 + (double)frustum[i][3] <= 0.0D && (double)frustum[i][0] * par1 + (double)frustum[i][1] * par9 + (double)frustum[i][2] * par5 + (double)frustum[i][3] <= 0.0D && (double)frustum[i][0] * par7 + (double)frustum[i][1] * par9 + (double)frustum[i][2] * par5 + (double)frustum[i][3] <= 0.0D && (double)frustum[i][0] * par1 + (double)frustum[i][1] * par3 + (double)frustum[i][2] * par11 + (double)frustum[i][3] <= 0.0D && (double)frustum[i][0] * par7 + (double)frustum[i][1] * par3 + (double)frustum[i][2] * par11 + (double)frustum[i][3] <= 0.0D && (double)frustum[i][0] * par1 + (double)frustum[i][1] * par9 + (double)frustum[i][2] * par11 + (double)frustum[i][3] <= 0.0D && (double)frustum[i][0] * par7 + (double)frustum[i][1] * par9 + (double)frustum[i][2] * par11 + (double)frustum[i][3] <= 0.0D)
            {
                return false;
            }
        }

        return true;
    }
}
