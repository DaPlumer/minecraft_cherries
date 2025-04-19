package daplumer.modregisterer.ModRegistries;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatFormatter;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

import static net.minecraft.stat.Stats.CUSTOM;

/**
 * This is a class that registers stats for mods
 * @implNote Use this
 */
public class ModStatTypeRegisterer implements ModDataRegisterer<Stat<Identifier>, StatFormatter>{
    private final String namespace;

    /**
     * @return the namespace of the local mod that is registering the stat.
     * @see ModDataRegisterer
     * @see ModStatTypeRegisterer
     * @see ModRegistries
     */
    @Override
    public String getNameSpace() {
        return this.namespace;
    }

    ModStatTypeRegisterer(String namespace){
        this.namespace = namespace;
    }

    /**
     * generate a statistic with a custom {@code name}.
     * for more information, see {@link ModDataRegisterer#register(String)}
     * in {@link ModDataRegisterer}.
     *
     * @see ModDataRegisterer
     * @see ModDataRegisterer#register(String)
     * @see Stat
     * @see Registries
     */
    @Override
    public Stat<Identifier> register(@NotNull String name, @Nullable StatFormatter instanceSettings, @Nullable Function<StatFormatter, Stat<Identifier>> instanceFactory) {
        Identifier identifier = getIdentifier(name);
        Registry.register(Registries.CUSTOM_STAT, name, identifier);
        return CUSTOM.getOrCreateStat(identifier, Objects.requireNonNullElse(instanceSettings,StatFormatter.DEFAULT));
    }

    /**
     * @return an instance of the data identified by the Identifier given. In the instance of the {@link ModStatTypeRegisterer}, this returns a {@link Stat} with an identifier type
     * @see ModRegistries
     */
    @Override
    public Stat<Identifier> getInstance(Identifier identifier) {
        return CUSTOM.getOrCreateStat(identifier);
    }

    /**
     * given an {@link Identifier}, return a registry key
     * @see ModStatTypeRegisterer
     * @see #register(String, StatFormatter, Function)
     */
    @Override
    public RegistryKey<Identifier> getRegistryKey(Identifier identifier) {
        return RegistryKey.of(RegistryKeys.CUSTOM_STAT,identifier);
    }
}
