package daplumer.more_food.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.ChickenVariant;
import net.minecraft.entity.passive.ChickenVariants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class Nest extends Block {
    public static MapCodec<Nest> CODEC = createCodec(Nest::new);
    public static final Item EGG = Items.EGG;
    public static final Item BROWN_EGG = Items.BROWN_EGG;
    public static final Item BLUE_EGG = Items.BLUE_EGG;

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }

    public static boolean isEgg(ItemStack item){
        if (item == null) return false;
        return item.isOf(EGG) || item.isOf(BROWN_EGG) || item.isOf(BLUE_EGG);
    }

    @Contract(pure = true)
    public static @Nullable Item getEggFromNumber(int n){
        return switch (n) {
            case 1  -> EGG;
            case 2  -> BROWN_EGG;
            case 3  -> BLUE_EGG;
            default -> null;
        };
    }

    public static int getNumberFromEgg(@Nullable ItemStack egg){
        if (egg == null)         return 0;
        if (egg.isOf(EGG))       return 1;
        if (egg.isOf(BROWN_EGG)) return 2;
        if (egg.isOf(BLUE_EGG))  return 3;
        throw new IndexOutOfBoundsException("non-egg item passed into the getNumberFromEgg function in Nest");

    }

    public static final IntProperty EGG_PROPERTY = IntProperty.of("egg",0,3);


    public static boolean hasEgg(@Nullable BlockState state){
        if(state == null) return false;
        return state.get(EGG_PROPERTY) != 0;
    }

    public static Item getEggFromProperty(@NotNull BlockState state){
        return getEggFromNumber(state.get(EGG_PROPERTY));
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(EGG_PROPERTY,0);
    }

    public Nest(Settings settings) {
        super(settings);
    }

    @Override
    protected boolean isTransparent(BlockState state) {
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(EGG_PROPERTY);
    }

    public BlockState setEgg(@NotNull BlockState state, ItemStack egg){
        return state.with(EGG_PROPERTY, getNumberFromEgg(egg));
    }

    public RegistryEntry<ChickenVariant> getVariant(BlockState state, ServerWorld world){
        DynamicRegistryManager registryManager = world.getRegistryManager();
        int eggVariant = state.get(EGG_PROPERTY);
        Registry<ChickenVariant> variantRegistry = registryManager.getOrThrow(RegistryKeys.CHICKEN_VARIANT);
        return variantRegistry.getEntry(variantRegistry.get(switch (eggVariant){
            case 1->ChickenVariants.TEMPERATE;
            case 2->ChickenVariants.WARM;
            default -> ChickenVariants.COLD;
        }));

    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isSideSolidFullSquare(world,pos.down(), Direction.UP);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if (!canPlaceAt(state,world,pos)) world.breakBlock(pos,true);
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(!hasEgg(state) || random.nextInt(25) != 1) return;
        ChickenEntity baby = EntityType.CHICKEN.spawn(world,pos, SpawnReason.TRIGGERED);
        assert baby != null;
        baby.setBaby(true);
        baby.setVariant(getVariant(state,world));
        world.setBlockState(pos,setEgg(state,null),NOTIFY_ALL_AND_REDRAW);
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(isEgg(stack)){// if the player is holding an egg
            if(hasEgg(state)) return ActionResult.CONSUME;
            world.playSoundAtBlockCenterClient(pos, SoundEvents.BLOCK_LEAF_LITTER_BREAK, SoundCategory.BLOCKS,1,1,true);
            world.setBlockState(pos,setEgg(state, stack));
            stack.decrementUnlessCreative(1, player);
            return ActionResult.SUCCESS;
        }
        if(hasEgg(state)){ //if the nest has an egg, take it from the nest
            world.playSoundAtBlockCenterClient(pos, SoundEvents.BLOCK_LEAF_LITTER_BREAK, SoundCategory.BLOCKS,1,1,true);
            player.giveOrDropStack(getEggFromProperty(state).getDefaultStack());
            world.setBlockState(pos,setEgg(state,null));
            return ActionResult.SUCCESS;
        }
        return ActionResult.CONSUME;
    }

    public static final VoxelShape outline = Block.createCuboidShape(3,0,3,13,6,13);

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return outline;
    }
}
