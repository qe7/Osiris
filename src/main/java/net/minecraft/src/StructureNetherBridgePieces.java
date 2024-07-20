package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class StructureNetherBridgePieces
{
    private static final StructureNetherBridgePieceWeight primaryComponents[];
    private static final StructureNetherBridgePieceWeight secondaryComponents[];

    public StructureNetherBridgePieces()
    {
    }

    private static ComponentNetherBridgePiece createNextComponentRandom(StructureNetherBridgePieceWeight par0StructureNetherBridgePieceWeight, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        Class class1 = par0StructureNetherBridgePieceWeight.field_40699_a;
        Object obj = null;

        if (class1 == (ComponentNetherBridgeStraight.class))
        {
            obj = ComponentNetherBridgeStraight.createValidComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (class1 == (ComponentNetherBridgeCrossing3.class))
        {
            obj = ComponentNetherBridgeCrossing3.createValidComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (class1 == (ComponentNetherBridgeCrossing.class))
        {
            obj = ComponentNetherBridgeCrossing.createValidComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (class1 == (ComponentNetherBridgeStairs.class))
        {
            obj = ComponentNetherBridgeStairs.createValidComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (class1 == (ComponentNetherBridgeThrone.class))
        {
            obj = ComponentNetherBridgeThrone.createValidComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (class1 == (ComponentNetherBridgeEntrance.class))
        {
            obj = ComponentNetherBridgeEntrance.createValidComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (class1 == (ComponentNetherBridgeCorridor5.class))
        {
            obj = ComponentNetherBridgeCorridor5.createValidComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (class1 == (ComponentNetherBridgeCorridor2.class))
        {
            obj = ComponentNetherBridgeCorridor2.createValidComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (class1 == (ComponentNetherBridgeCorridor.class))
        {
            obj = ComponentNetherBridgeCorridor.createValidComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (class1 == (ComponentNetherBridgeCorridor3.class))
        {
            obj = ComponentNetherBridgeCorridor3.createValidComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (class1 == (ComponentNetherBridgeCorridor4.class))
        {
            obj = ComponentNetherBridgeCorridor4.createValidComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (class1 == (ComponentNetherBridgeCrossing2.class))
        {
            obj = ComponentNetherBridgeCrossing2.createValidComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (class1 == (ComponentNetherBridgeNetherStalkRoom.class))
        {
            obj = ComponentNetherBridgeNetherStalkRoom.createValidComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }

        return ((ComponentNetherBridgePiece)(obj));
    }

    static ComponentNetherBridgePiece createNextComponent(StructureNetherBridgePieceWeight par0StructureNetherBridgePieceWeight, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        return createNextComponentRandom(par0StructureNetherBridgePieceWeight, par1List, par2Random, par3, par4, par5, par6, par7);
    }

    static StructureNetherBridgePieceWeight[] getPrimaryComponents()
    {
        return primaryComponents;
    }

    static StructureNetherBridgePieceWeight[] getSecondaryComponents()
    {
        return secondaryComponents;
    }

    static
    {
        primaryComponents = (new StructureNetherBridgePieceWeight[]
                {
                    new StructureNetherBridgePieceWeight(ComponentNetherBridgeStraight.class, 30, 0, true), new StructureNetherBridgePieceWeight(ComponentNetherBridgeCrossing3.class, 10, 4), new StructureNetherBridgePieceWeight(ComponentNetherBridgeCrossing.class, 10, 4), new StructureNetherBridgePieceWeight(ComponentNetherBridgeStairs.class, 10, 3), new StructureNetherBridgePieceWeight(ComponentNetherBridgeThrone.class, 5, 2), new StructureNetherBridgePieceWeight(ComponentNetherBridgeEntrance.class, 5, 1)
                });
        secondaryComponents = (new StructureNetherBridgePieceWeight[]
                {
                    new StructureNetherBridgePieceWeight(ComponentNetherBridgeCorridor5.class, 25, 0, true), new StructureNetherBridgePieceWeight(ComponentNetherBridgeCrossing2.class, 15, 5), new StructureNetherBridgePieceWeight(ComponentNetherBridgeCorridor2.class, 5, 10), new StructureNetherBridgePieceWeight(ComponentNetherBridgeCorridor.class, 5, 10), new StructureNetherBridgePieceWeight(ComponentNetherBridgeCorridor3.class, 10, 3, true), new StructureNetherBridgePieceWeight(ComponentNetherBridgeCorridor4.class, 7, 2), new StructureNetherBridgePieceWeight(ComponentNetherBridgeNetherStalkRoom.class, 5, 2)
                });
    }
}
