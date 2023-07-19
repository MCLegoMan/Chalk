package de.dafuqs.chalk.common.items;

import de.dafuqs.chalk.common.Chalk;
import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;

public class GlowChalkItem extends ChalkItem {

    public GlowChalkItem(Settings settings, DyeColor dyeColor) {
        super(settings, dyeColor);
    }
    
    public Block getChalkMarkBlock() {
        return Chalk.chalkVariants.get(this.dyeColor).glowChalkBlock;
    }

}
