package net.minecraft.src;

public class Frustrum implements ICamera
{
    private ClippingHelper clippingHelper;
    private double xPosition;
    private double yPosition;
    private double zPosition;

    public Frustrum()
    {
        clippingHelper = ClippingHelperImpl.getInstance();
    }

    public void setPosition(double par1, double par3, double par5)
    {
        xPosition = par1;
        yPosition = par3;
        zPosition = par5;
    }

    /**
     * Calls the clipping helper. Returns true if the box is inside all 6 clipping planes, otherwise returns false.
     */
    public boolean isBoxInFrustum(double par1, double par3, double par5, double par7, double par9, double par11)
    {
        return clippingHelper.isBoxInFrustum(par1 - xPosition, par3 - yPosition, par5 - zPosition, par7 - xPosition, par9 - yPosition, par11 - zPosition);
    }

    /**
     * Returns true if the bounding box is inside all 6 clipping planes, otherwise returns false.
     */
    public boolean isBoundingBoxInFrustum(AxisAlignedBB par1AxisAlignedBB)
    {
        return isBoxInFrustum(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ, par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
    }
}
