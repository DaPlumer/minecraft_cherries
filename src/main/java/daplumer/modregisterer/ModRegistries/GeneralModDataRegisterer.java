package daplumer.modregisterer.ModRegistries;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
/**
 * This is a class made to merge most methods of different
 * data types to make updating data registration methods easier and centralize data registration methods.
 * @see ModDataRegisterer ModDataRegisterer
 * @see ModRegistries
 * @see #register(String, Object, Function)
 * @see Registry
 */
public class GeneralModDataRegisterer<T, S> implements ModDataRegisterer<T,S> {
    public final String namespace;
    public final Registry<T> registry;
    public final BiFunction<S, RegistryKey<T>, S> registryKeySettingsFactory;
    public final Function<S,T> defaultDataFactory;
    public final Supplier<S> defaultSettingsFactory;

    /**
     * @param registry this Is the registry identifier used by the class instance and
     * {@code registryKeySettingsFactory} and {@code defaultInstanceFactory} are lambda functions manipulating data settings.
     * This function registers lambda functions associated with data {@link Registry} and the {@link Registry} type
     * @see Registry
     * @see ModRegistries
     * @see #register(String, Object, Function)
     */
    GeneralModDataRegisterer(Registry<T> registry, String namespace, BiFunction<S, RegistryKey<T>, S> registryKeySettingsFactory, Function<S,T> defaultInstanceFactory, Supplier<S> defaultSettingsFactory){
        this.namespace = namespace;
        this.registry = registry;
        this.registryKeySettingsFactory = registryKeySettingsFactory;//This lambda function applies registry keys to settings
        this.defaultDataFactory = defaultInstanceFactory;//This lambda function creates an instance of the data using settings provided
        this.defaultSettingsFactory = defaultSettingsFactory;//This lambda function supplies the default settings when a parameter is ignored
    }
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

    /**
     * @param name The name of the object being registered
     * @param instanceSettings The settings of the data being registered
     * @param instanceFactory Lambda function that turns the data settings into instances. This is so subclasses of objects can be registered too.
     * @summary This function registers a data instance given instance settings
     * @see ModRegistries
     * @see Registry
     */
    @Override
    public T register(@NotNull String name, @Nullable S instanceSettings, @Nullable Function<S, T> instanceFactory){
        RegistryKey<T> key = this.getRegistryKey(name); //create registry key for item
        S settings = Objects.requireNonNullElse(instanceSettings,defaultSettingsFactory.get()); // make settings nonNull
        registryKeySettingsFactory.apply(settings,key);
        Function<S,T> factory = Objects.requireNonNullElse(instanceFactory,defaultDataFactory); // data factory nonNull
        T dataInstance = factory.apply(settings);// apply settings key and turn into data instance

        return Registry.register(this.registry, key, dataInstance);
    }

    @Override
    public T getInstance(Identifier identifier) {
        return this.registry.get(identifier);
    }

    @Override
    public RegistryKey<T> getRegistryKey(String name) {
        return getRegistryKey(getIdentifier(name));
    }

    @Override
    public RegistryKey<T> getRegistryKey(Identifier identifier) {
        return RegistryKey.of(this.registry.getKey(), identifier);
    }

}
