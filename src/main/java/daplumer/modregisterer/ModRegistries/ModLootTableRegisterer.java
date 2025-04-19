package daplumer.modregisterer.ModRegistries;

import daplumer.more_bugs.mixin.LootTableModificationMixin;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@SuppressWarnings("unchecked")
public class ModLootTableRegisterer implements ModDataRegisterer<RegistryKey<LootTable>, RegistryKey<LootTable>> {
    private final String namespace;
    @Override
    public String getNameSpace() {
        return namespace;
    }

    ModLootTableRegisterer(String namespace){
        this.namespace = namespace;
    }
    @Override
    public RegistryKey<LootTable> register(@NotNull String name,@Nullable RegistryKey<LootTable> instanceSettings,@Nullable Function<RegistryKey<LootTable>,@Nullable RegistryKey<LootTable>> instanceFactory) {
        RegistryKey<LootTable> key = getRegistryKey(name);
        return LootTableModificationMixin.registerLootTable(key);
    }

    @Override
    public RegistryKey<LootTable> getInstance(Identifier identifier) {
        return null;
    }
    @Override
    public RegistryKey<LootTable> getRegistryKey(Identifier identifier) {
        return RegistryKey.of(RegistryKeys.LOOT_TABLE, identifier);
    }
}
