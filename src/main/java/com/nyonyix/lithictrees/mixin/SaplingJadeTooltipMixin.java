package com.nyonyix.lithictrees.mixin;

import com.mojang.logging.LogUtils;
import com.nyonyix.lithictrees.LithicTrees;
import com.nyonyix.lithictrees.data.TreeClimateLoader;
import com.nyonyix.lithictrees.utils.ModTags;
import net.dries007.tfc.common.blockentities.TickCounterBlockEntity;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.dries007.tfc.common.blocks.wood.TFCSaplingBlock;
import net.dries007.tfc.compat.jade.common.BlockEntityTooltip;

import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.climate.Climate;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import static net.dries007.tfc.compat.jade.common.BlockEntityTooltips.timeLeft;

@Mixin(targets = "net.dries007.tfc.compat.jade.common.BlockEntityTooltips")
public class SaplingJadeTooltipMixin {

    private static final Logger LOGGER = LogUtils.getLogger();

    @Shadow
    @Final
    @Mutable
    public static BlockEntityTooltip SAPLING;

    static {

      LOGGER.info("{}: Attempting TFC Sapling Jade Tooltip", LithicTrees.MODID);

      SAPLING = (level, state, pos, entity, tooltip) -> {

          if (entity instanceof TickCounterBlockEntity counter) {
              Block sapling_block = state.getBlock();
              if (sapling_block instanceof TFCSaplingBlock sapling) {

                  boolean isValidClimate = true;

                  if (ForgeRegistries.BLOCKS.tags().getTag(ModTags.Blocks.SAPLINGS).contains(sapling_block)) {

                      TreeClimateLoader.TreeClimate sapling_climate = TreeClimateLoader.getClimateForTree(sapling_block);

                      float env_temp = Climate.getAverageTemperature(level, pos);
                      float env_rainfall = Climate.getRainfall(level, pos);
                      int env_hydration = FarmlandBlock.getHydration(level, pos.below());

                      float[] sapling_temperature = sapling_climate.temperature();
                      float[] sapling_rainfall = sapling_climate.rainfall();

                      if (sapling_climate.isTemperatureLow(env_temp)) {
                          tooltip.accept(Component.literal("Temperature too low!").withStyle(ChatFormatting.DARK_RED));
                          tooltip.accept(Component.literal(String.format("Current: %.1f Needs: %.1f -> %.1f", env_temp, sapling_temperature[0], sapling_temperature[1])));

                      } else if (sapling_climate.isTemperatureHigh(env_temp)) {
                          tooltip.accept(Component.literal("Temperature too high!").withStyle(ChatFormatting.DARK_RED));
                          tooltip.accept(Component.literal(String.format("Current: %.1f Needs: %.1f -> %.1f", env_temp, sapling_temperature[0], sapling_temperature[1])));
                      } else {
                          tooltip.accept(Component.literal("Temperature is ok").withStyle(ChatFormatting.DARK_GREEN));
                          tooltip.accept(Component.literal(String.format("Current: %.1f Needs: %.1f -> %.1f", env_temp,sapling_temperature[0], sapling_temperature[1])));
                      }

                      if(sapling_climate.isRainfallLow(env_rainfall)) {
                          tooltip.accept(Component.literal("Rainfall too low!").withStyle(ChatFormatting.DARK_RED));
                          tooltip.accept(Component.literal(String.format("Current: %.1f Needs: %.1f -> %.1f", env_rainfall, sapling_rainfall[0], sapling_rainfall[1])));
                      } else if (sapling_climate.isRainfallHigh(env_rainfall)){
                          tooltip.accept(Component.literal("Rainfall too high!").withStyle(ChatFormatting.DARK_RED));
                          tooltip.accept(Component.literal(String.format("Current: %.1f Needs: %.1f -> %.1f", env_rainfall, sapling_rainfall[0], sapling_rainfall[1])));
                      } else {
                          tooltip.accept(Component.literal("Rainfall is ok").withStyle(ChatFormatting.DARK_GREEN));
                          tooltip.accept(Component.literal(String.format("Current: %.1f Needs: %.1f -> %.1f", env_rainfall, sapling_rainfall[0], sapling_rainfall[1])));
                      }

                      isValidClimate = (sapling_climate.isValidTemperature(env_temp) && sapling_climate.isValidRainfall(env_rainfall));
                      tooltip.accept(Component.literal(""));

                  }

                  if (isValidClimate) {
                      timeLeft(level, tooltip, (long)((double)(sapling.getDaysToGrow() * 24000) * (Double) TFCConfig.SERVER.globalSaplingGrowthModifier.get()) - counter.getTicksSinceUpdate(), Component.translatable("tfc.jade.ready_to_grow"));
                  } else {
                      tooltip.accept(Component.literal("Time Left: Unable To grow!"));
                  }
              }
          }
      };

      LOGGER.info("{}: Replaced TFC Sapling Jade Tooltip", LithicTrees.MODID);

    };

}
