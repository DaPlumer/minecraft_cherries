package daplumer.minecraft_cherries;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.condition.TableBonusLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.RegistryKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CherriesForMinecraft implements ModInitializer {
	public static final String MOD_ID = "minecraft_cherries";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final RegistryKey<LootTable> CHERRY_LEAVES_LOOT_TABLE_ID = Blocks.CHERRY_LEAVES.getLootTableKey().get();
	public static final Item CHERRY = CherriesForMinecraftRegistries.ITEMS.register("cherry",new Item.Settings().food(FoodComponents.APPLE));

	@Override
	public void onInitialize() {
		CherriesForMinecraftRegistries.Initialize();

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> entries.addAfter(Items.APPLE,CHERRY));

		//register loot tables for fruit
		LootTableEvents.MODIFY.register(((key, tableBuilder, source, registries) -> {
			if (source.isBuiltin() && CHERRY_LEAVES_LOOT_TABLE_ID.equals(key)){
				LootPool.Builder poolBuilder = LootPool.builder()
						.with(ItemEntry.builder(CHERRY)
								.conditionally(SurvivesExplosionLootCondition.builder())
								.conditionally(TableBonusLootCondition.builder(registries.getEntryOrThrow(Enchantments.FORTUNE),0.005f,0.005555f,0.00625f,0.008333f,0.025f)));
				tableBuilder.pool(poolBuilder);
			}
		}));
	}
}