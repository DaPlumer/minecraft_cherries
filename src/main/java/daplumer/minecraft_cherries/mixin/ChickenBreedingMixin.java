package daplumer.minecraft_cherries.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChickenEntity.class)
public abstract class ChickenBreedingMixin extends AnimalEntity {

    protected ChickenBreedingMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        if (this.forEachGiftedItem(world, LootTables.CHICKEN_LAY_GAMEPLAY, this::dropStack)) {
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.emitGameEvent(GameEvent.ENTITY_PLACE);
        }
        breed(world, (AnimalEntity) entity,null);
        return null;
    }
}
