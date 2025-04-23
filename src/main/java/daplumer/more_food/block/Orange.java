package daplumer.more_food.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

public class Orange extends Block {
    public Orange(Settings settings) {
        super(settings);
    }
    public static final VoxelShape outline = Block.createCuboidShape(4,2,4,12,10,12);
    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (world.getBlockState(pos.up()).isAir()) return false;
        return world.getBlockState(pos.up()).getCollisionShape(world, pos.up()).getBoundingBox().minY == 0;
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if(!canPlaceAt(state,world,pos)) world.breakBlock(pos,true);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return outline;
    }
}
