package daplumer.modregisterer.ModRegistries;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@SuppressWarnings("unused")
public interface ModDataRegisterer<T,S> {
    String getNameSpace();
    T register(@NotNull String name,@Nullable S instanceSettings,@Nullable Function<S,T> instanceFactory);

    default Identifier getIdentifier(@NotNull String name) {
        return Identifier.of(getNameSpace(), name);
    }

    default T getInstance(String name){
        return getInstance(getIdentifier(name));
    }

    T getInstance(Identifier identifier);
    @SuppressWarnings("rawtypes")
    default RegistryKey getRegistryKey(String name){
        return getRegistryKey(getIdentifier(name));
    }
    @SuppressWarnings("rawtypes")
    RegistryKey getRegistryKey(Identifier identifier);
    default T register(String name, @Nullable S instanceSettings) {
        return this.register(name, instanceSettings, null);
    }
    default T register(String name) {
        return this.register(name, null, null);
    }
}
