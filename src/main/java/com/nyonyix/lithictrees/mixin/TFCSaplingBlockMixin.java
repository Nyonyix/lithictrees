package com.nyonyix.lithictrees.mixin;

import com.mojang.logging.LogUtils;
import com.nyonyix.lithictrees.data.TreeClimateLoader;
import com.nyonyix.lithictrees.utils.ModTags;
import net.dries007.tfc.common.blocks.wood.TFCSaplingBlock;
import net.dries007.tfc.util.climate.Climate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TFCSaplingBlock.class)
public class TFCSaplingBlockMixin {

    private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci){

        if (ForgeRegistries.BLOCKS.tags().getTag(ModTags.Blocks.SAPLINGS).contains(state.getBlock())) {

            TreeClimateLoader.TreeClimate treeClimate = TreeClimateLoader.getClimateForTree(state.getBlock());
            float envTemperature = Climate.getAverageTemperature(level, pos);
            float envRainfall = Climate.getRainfall(level, pos);

            if (!treeClimate.isValidTemperature(envTemperature) || !treeClimate.isValidRainfall(envRainfall)) {
                ci.cancel();
            }

        }

    }

}
