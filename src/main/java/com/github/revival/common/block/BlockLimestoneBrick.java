package com.github.revival.common.block;

import com.github.revival.common.handler.LocalizationStrings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockLimestoneBrick extends Block
{

    public BlockLimestoneBrick(Material material)
    {
        super(material.rock);
        setHardness(3.0F);
        setResistance(20.0F);
        setStepSound(Block.soundTypeStone);
        setBlockName(LocalizationStrings.BLOCK_LIMESTONEBRICK_NAME);
    }

    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("fossil:Limestone_Brick");
    }

}
