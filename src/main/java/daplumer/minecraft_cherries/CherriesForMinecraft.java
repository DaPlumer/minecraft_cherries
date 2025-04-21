package daplumer.minecraft_cherries;

import daplumer.minecraft_cherries.block.Nest;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.condition.TableBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static daplumer.minecraft_cherries.CherriesForMinecraftRegistries.*;

@SuppressWarnings("unused")
public class CherriesForMinecraft implements ModInitializer {
    public static final  String MOD_ID = "minecraft_cherries";
	public static Logger LOGGER = Logger.getLogger(MOD_ID);
	public static final Item CHERRY = ITEMS.register("cherry",new Item.Settings().food(FoodComponents.APPLE));

	public static final Item ORANGE = ITEMS.register("orange",new Item.Settings().food(FoodComponents.APPLE));
	public static final Block NEST_BLOCK = BLOCKS.register("nest",AbstractBlock.Settings.create().breakInstantly().burnable().pistonBehavior(PistonBehavior.DESTROY).nonOpaque().sounds(BlockSoundGroup.GRASS), Nest::new);
	public static final BlockItem NEST_ITEM = (BlockItem) ITEMS.register("nest",new Item.Settings(),BLOCK_ITEM(NEST_BLOCK));

    public static final Map<RegistryKey<LootTable>, Item> droppedFruits = new HashMap<>();

	public static final Item CHERRY_JUICE = ITEMS.register("cherry_juice", new Item.Settings()
			.food(FoodComponents.HONEY_BOTTLE)
			.component(DataComponentTypes.CONSUMABLE, ConsumableComponents.DRINK)
			.useRemainder(Items.GLASS_BOTTLE));
	public static final Item ORANGE_JUICE = ITEMS.register("orange_juice", new Item.Settings()
			.food(FoodComponents.HONEY_BOTTLE)
			.component(DataComponentTypes.CONSUMABLE, ConsumableComponents.DRINK)
			.useRemainder(Items.GLASS_BOTTLE));

	@SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
	public void onInitialize() {
		droppedFruits.put(Blocks.CHERRY_LEAVES.getLootTableKey().get(),CHERRY);
		droppedFruits.put(Blocks.JUNGLE_LEAVES.getLootTableKey().get(),ORANGE);
		CherriesForMinecraftRegistries.Initialize();

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
			entries.addAfter(Items.APPLE,CHERRY);
			entries.addAfter(Items.HONEY_BOTTLE,CHERRY_JUICE);

			entries.addAfter(CHERRY,ORANGE);

			entries.addAfter(CHERRY_JUICE,ORANGE_JUICE);
		});


		//register loot tables for fruit
		LootTableEvents.MODIFY.register(((key, tableBuilder, source, registries) -> {
			if (source.isBuiltin() && droppedFruits.containsKey(key)){
				LootPool.Builder poolBuilder = LootPool.builder()
						.with(ItemEntry.builder(droppedFruits.get(key))
								.conditionally(SurvivesExplosionLootCondition.builder())
								.conditionally(TableBonusLootCondition.builder(registries.getEntryOrThrow(Enchantments.FORTUNE),0.005f,0.005555f,0.00625f,0.008333f,0.025f)));
				tableBuilder.pool(poolBuilder);
			}
		}));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((entries ->
			entries.addAfter(Items.FIREFLY_BUSH,NEST_ITEM)
		));
	}
}