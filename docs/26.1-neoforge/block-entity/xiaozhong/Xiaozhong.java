package org.teacon.xiaozhong;

import com.mojang.datafixers.DSL;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import org.jetbrains.annotations.NotNull;

@Mod("xiaozhong")
public class Xiaozhong {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.Blocks.createBlocks("xiaozhong");
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "xiaozhong");

    public static final DeferredHolder<Block, MyMachine> MY_MACHINE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MyMachineEntity>> MY_MACHINE_BLOCK_ENTITY;

    static {
        MY_MACHINE = BLOCKS.register("my_machine",
                () -> new MyMachine(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
        MY_MACHINE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("my_machine",
                () -> BlockEntityType.Builder.of(MyMachineEntity::new, MY_MACHINE.get()).build(DSL.remainderType()));
    }

    public Xiaozhong(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }

    public static final class MyMachine extends BaseEntityBlock {

        public static final MapCodec<MyMachine> CODEC = simpleCodec(MyMachine::new);
        public MyMachine(Properties props) {
            super(props);
        }

        @Override
        protected MapCodec<? extends BaseEntityBlock> codec() {
            return CODEC;
        }

        @NotNull
        @Override
        public RenderShape getRenderShape(@NotNull BlockState state) {
            return RenderShape.MODEL;
        }

        @Override
        public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
            return new MyMachineEntity(pos, state);
        }

        @Override
        public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
            return level.isClientSide() ? null : createTickerHelper(blockEntityType, MY_MACHINE_BLOCK_ENTITY.get(), MyMachineEntity::tick);
        }
    }

    public static final class MyMachineEntity extends BlockEntity {
        public MyMachineEntity(BlockEntityType<MyMachineEntity> type, BlockPos worldPosition, BlockState blockState) {
            super(type, worldPosition, blockState);
        }

        public MyMachineEntity(BlockPos worldPosition, BlockState blockState) {
            this(MY_MACHINE_BLOCK_ENTITY.get(), worldPosition, blockState);
        }

        private int count = 0;

        public static void tick(Level level, BlockPos pos, BlockState state, MyMachineEntity entity) {
            entity.count += 1;
            if (entity.count > 100) {
                entity.count = 0;
                if (level != null && !level.isClientSide()) {
                    var player = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5.0, false);
                    if (player != null) {
                        player.sendSystemMessage(Component.translatable("chat.xiaozhong.welcome"));
                    }
                }
            }
        }
    }
}
