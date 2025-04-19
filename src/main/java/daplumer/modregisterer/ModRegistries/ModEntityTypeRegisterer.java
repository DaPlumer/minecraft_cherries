package daplumer.modregisterer.ModRegistries;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ModEntityTypeRegisterer implements ModDataRegisterer<EntityType, EntityType.Builder>{
    private final String namespace;

    ModEntityTypeRegisterer(String namespace){
        this.namespace = namespace;
    }
    @Override
    public String getNameSpace() {
        return namespace;
    }

    @Override
    public EntityType register(@NotNull String name, EntityType.@Nullable Builder instanceSettings, @Nullable Function<EntityType.Builder, EntityType> instanceFactory) {
        if(instanceSettings == null){
            Logger logger = Logger.getLogger("ModRegistrationLib");
            logger.log(Level.SEVERE, "Null instanceSettings passed into the Entity registration function under namespace; " + this.getNameSpace());
            logger.log(Level.FINE, "Although most custom registerers under the Mod Registration Library allow use of null pointers, the EntityRegisterer does not");
            throw new NullPointerException("Null pointers passed into the ModEntityTypeRegisterer.register function");
        }
        return Registry.register(Registries.ENTITY_TYPE, this.getRegistryKey(name), instanceSettings.build(this.getRegistryKey(name)));
    }

    @Override
    public EntityType getInstance(Identifier identifier) {
        return Registries.ENTITY_TYPE.get(identifier);
    }

    @Override
    public RegistryKey getRegistryKey(Identifier identifier) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, identifier);
    }
}