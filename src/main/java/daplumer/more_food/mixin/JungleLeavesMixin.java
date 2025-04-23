package daplumer.more_food.mixin;

import daplumer.more_food.MoreFood;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LeavesBlock.class)
public class JungleLeavesMixin extends Block implements Fertilizable {
    public JungleLeavesMixin(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.down()).isAir() && state.getRegistryEntry().matchesId(Identifier.of("minecraft","jungle_leaves"));
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return state.getRegistryEntry().matchesId(Identifier.of("minecraft","jungle_leaves"));
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        world.setBlockState(pos.down(), MoreFood.ORANGE_BLOCK.getDefaultState(), LeavesBlock.NOTIFY_LISTENERS);

    }
}
