package de.dafuqs.chalk.common;

import de.dafuqs.chalk.common.blocks.ChalkMarkBlock;
import de.dafuqs.chalk.common.blocks.GlowChalkMarkBlock;
import de.dafuqs.chalk.common.data.ChalkCompatibilityData;
import de.dafuqs.chalk.common.data.ChalkData;
import de.dafuqs.chalk.common.data.ChalkLoggerType;
import de.dafuqs.chalk.common.items.ChalkItem;
import de.dafuqs.chalk.common.items.GlowChalkItem;
import de.dafuqs.chalk.common.pattern.ChalkPatterns;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;

import java.util.HashMap;

public class Chalk implements ModInitializer {
	public static GameRules.Key<EnumRule<ChalkPatterns>> chalkDefaultPattern = GameRuleRegistry.register("chalk:defaultPattern", GameRules.Category.MISC, GameRuleFactory.createEnumRule(ChalkPatterns.NORMAL));
	public static GameRules.Key<GameRules.BooleanRule> chalkCanChangePatterns = GameRuleRegistry.register("chalk:canChangePatterns", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));
	
	public static class ChalkVariant {
		String colorString;
		int color;
		public Item chalkItem;
		public Block chalkBlock;
		public Item glowChalkItem;
		public Block glowChalkBlock;
		
		public ChalkVariant(DyeColor dyeColor, int color, String colorString) {
			this.color = color;
			this.colorString = colorString;
			this.chalkItem = new ChalkItem(new Item.Settings().maxCount(1).maxDamage(64), dyeColor);
			this.chalkBlock = new ChalkMarkBlock(AbstractBlock.Settings.create().replaceable().noCollision().nonOpaque().sounds(BlockSoundGroup.GRAVEL).pistonBehavior(PistonBehavior.DESTROY), dyeColor);
			this.glowChalkItem = new GlowChalkItem(new Item.Settings().maxCount(1).maxDamage(64), dyeColor);
			this.glowChalkBlock = new GlowChalkMarkBlock(AbstractBlock.Settings.create().replaceable().noCollision().nonOpaque().sounds(BlockSoundGroup.GRAVEL)
					.luminance((state) -> ChalkCompatibilityData.CONTINUITY ? 0 : 1)
					.postProcess(ChalkCompatibilityData.CONTINUITY ? Chalk::never : Chalk::always)
					.emissiveLighting(ChalkCompatibilityData.CONTINUITY ? Chalk::never : Chalk::always)
					.pistonBehavior(PistonBehavior.DESTROY), dyeColor);
			this.ItemGroups();
		}

		public void ItemGroups() {
			/* Chalk ItemGroups: Functional Blocks, Tools and Utilities */
			ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(this.chalkItem));
			ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(this.chalkItem));
			/* Glow Chalk ItemGroups: Functional Blocks, Tools and Utilities */
			ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(this.glowChalkItem));
			ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(this.glowChalkItem));
		}
		
		public void register() {
			registerBlock(colorString + "chalk_mark", chalkBlock);
			registerItem(colorString + "chalk", chalkItem);
			registerBlock(colorString + "glow_chalk_mark", glowChalkBlock);
			registerItem(colorString + "glow_chalk", glowChalkItem);
		}
		
		public void registerClient() {
			BlockRenderLayerMap.INSTANCE.putBlock(this.chalkBlock, RenderLayer.getCutout());
			BlockRenderLayerMap.INSTANCE.putBlock(this.glowChalkBlock, RenderLayer.getCutout());
			
			ColorProviderRegistry.BLOCK.register((state, world, pos, index) -> color, chalkBlock);
			ColorProviderRegistry.BLOCK.register((state, world, pos, index) -> color, glowChalkBlock);
		}
	}
	
	public HashMap<DyeColor, Integer> dyeColors = new HashMap<>() {{
		put(DyeColor.BLACK, 0x171717);
		put(DyeColor.BLUE, 0x2c2e8e);
		put(DyeColor.BROWN, 0x613c20);
		put(DyeColor.CYAN, 0x157687);
		put(DyeColor.GRAY, 0x292929);
		put(DyeColor.GREEN, 0x495b24);
		put(DyeColor.LIGHT_BLUE, 0x258ac8);
		put(DyeColor.LIGHT_GRAY, 0x8b8b8b);
		put(DyeColor.LIME, 0x5faa19);
		put(DyeColor.MAGENTA, 0xaa32a0);
		put(DyeColor.ORANGE, 0xe16201);
		put(DyeColor.PINK, 0xd6658f);
		put(DyeColor.PURPLE, 0x641f9c);
		put(DyeColor.RED, 0x8f2121);
		put(DyeColor.WHITE, 0xFFFFFF);
		put(DyeColor.YELLOW, 0xf0ff15);
	}};
	
	public static HashMap<DyeColor, ChalkVariant> chalkVariants = new HashMap<>();
	
	private static boolean always(BlockState blockState, BlockView blockView, BlockPos blockPos) {
		return true;
	}
	
	private static boolean never(BlockState blockState, BlockView blockView, BlockPos blockPos) {
		return false;
	}
	
	private static void registerBlock(String name, Block block) {
		Registry.register(Registries.BLOCK, new Identifier(ChalkData.ID, name), block);
	}
	
	private static void registerItem(String name, Item item) {
		Registry.register(Registries.ITEM, new Identifier(ChalkData.ID, name), item);
	}
	
	@Override
	public void onInitialize() {
		ChalkData.sendToLog("Registering blocks and items...", ChalkLoggerType.INFO);

		/*
		 * colored chalk variants are only added if the colorful addon is installed
		 * this allows chalk to use the "chalk" mod to use the chalk namespace for all functionality
		 * while still having it configurable / backwards compatible
		 */

		ChalkVariant chalkVariant;
		for (DyeColor dyeColor : DyeColor.values()) {
			int color = dyeColors.get(dyeColor);
			if (dyeColor.equals(DyeColor.WHITE)) {
				/* backwards compatibility */
				chalkVariant = new ChalkVariant(dyeColor, color, "");
				chalkVariant.register();
				chalkVariants.put(dyeColor, chalkVariant);
			} else if (ChalkCompatibilityData.COLORFUL_ADDON) {
				/* if colourful addon present */
				chalkVariant = new ChalkVariant(dyeColor, color, dyeColor + "_");
				chalkVariant.register();
				chalkVariants.put(dyeColor, chalkVariant);
			}
		}
		ChalkData.sendToLog("Startup finished!", ChalkLoggerType.INFO);
	}
}