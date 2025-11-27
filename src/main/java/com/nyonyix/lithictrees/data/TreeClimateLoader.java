package com.nyonyix.lithictrees.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.nyonyix.lithictrees.LithicTrees;
import com.nyonyix.lithictrees.utils.ModTags;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = LithicTrees.MODID)
public class TreeClimateLoader {

    public static record TreeClimate(float[] temperature, float[] rainfall) {

        public boolean isValidTemperature(float temp) {return (temp > this.temperature[0] && temp < this.temperature[1]);}
        public boolean isTemperatureHigh(float temp) {return (temp > this.temperature[1]);}
        public boolean isTemperatureLow(float temp) {return (temp < this.temperature[0]);}

        public boolean isValidRainfall(float rain) {return (rain > this.rainfall[0] && rain < this.rainfall[1]);}
        public boolean isRainfallHigh(float rain) {return  (rain > this.rainfall[1]);}
        public boolean isRainfallLow(float rain) {return (rain < this.rainfall[0]);}
    }

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String RESOURCE_PATH = "tree_climates";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<ResourceLocation, TreeClimate> requirements = new HashMap<>();
    private static final Map<Block, TreeClimate> blockToClimate = new HashMap<>();

    public static Map<Block, TreeClimate> getBlockToClimate() {return Map.copyOf(blockToClimate);}
    public static TreeClimate getClimateForTree(Block block) {return blockToClimate.get(block);}

    @SubscribeEvent
    public static void onResourceReload(AddReloadListenerEvent event) {

        event.addListener(new SimplePreparableReloadListener<Map<ResourceLocation, TreeClimate>> () {
            @Override
            protected Map<ResourceLocation, TreeClimate> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {

                Map<ResourceLocation, TreeClimate> loadedData = new HashMap<>();
                Map<ResourceLocation, Resource> resources = resourceManager.listResources(RESOURCE_PATH, location -> location.getPath().endsWith(".json"));

                LOGGER.info("Loading {} climate files", resources.size());


                for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(entry.getValue().open(), StandardCharsets.UTF_8))) {

                        JsonObject json = GSON.fromJson(reader, JsonObject.class);

                        float[] rainfall = new float[2];
                        rainfall[0] = json.getAsJsonObject("tree").getAsJsonArray("rainfall_range").get(0).getAsFloat();
                        rainfall[1] = json.getAsJsonObject("tree").getAsJsonArray("rainfall_range").get(1).getAsFloat();

                        float[] temperature = new float[2];
                        temperature[0] = json.getAsJsonObject("tree").getAsJsonArray("temperature_range").get(0).getAsFloat();
                        temperature[1] = json.getAsJsonObject("tree").getAsJsonArray("temperature_range").get(1).getAsFloat();

                        ResourceLocation saplingResourceLoc = new ResourceLocation(json.getAsJsonObject("tree").get("id").getAsString());

                        TreeClimate sapling = new TreeClimate(temperature, rainfall);

                        loadedData.put(saplingResourceLoc, sapling);


                    } catch (IOException | NullPointerException e) {
                        LOGGER.error("Failed to load: {}", entry.getValue(), e);
                    }
                }
                LOGGER.info("Loaded {} climate files", loadedData.size());
                return  loadedData;
            }

            @Override
            protected void apply(Map<ResourceLocation, TreeClimate> data, ResourceManager resourceManager, ProfilerFiller profiler) {

                requirements.clear();
                requirements.putAll(data);
                blockToClimate.clear();
                int blocksMapped = 0;

                for (Map.Entry<ResourceLocation, TreeClimate> entry : data.entrySet()) {
                    Block block = ForgeRegistries.BLOCKS.getValue(entry.getKey());
                    if(block != null && !block.defaultBlockState().isAir()) {
                        blockToClimate.put(block, entry.getValue());
                        blocksMapped++;
                    }
                }
                LOGGER.info("{} climate targets mapped", blocksMapped);
            }
        });

    }

    @SubscribeEvent
    public static void onTagsUpdatedEvent(TagsUpdatedEvent event) {

        Set<ResourceLocation> tagSet = event.getRegistryAccess()
                .registryOrThrow(Registries.BLOCK)
                .getTag(ModTags.Blocks.SAPLINGS)
                .stream()
                .flatMap(holders -> holders.stream())
                .map(blockHolder -> ForgeRegistries.BLOCKS.getKey(blockHolder.value()))
                .collect(Collectors.toSet());
        Set<ResourceLocation> jsonSet = requirements.keySet();

        Set<ResourceLocation> itemsNotInJson = new HashSet<>(tagSet);
        itemsNotInJson.removeAll(jsonSet);

        Set<ResourceLocation> itemsNotInTag = new HashSet<>(jsonSet);
        itemsNotInTag.removeAll(tagSet);

        if (!itemsNotInJson.isEmpty()) {
            for (ResourceLocation entry : itemsNotInJson) {
                LOGGER.warn("Climate data not found for: {}", entry.toString());
            }
        }
        if (!itemsNotInTag.isEmpty()) {
            for (ResourceLocation entry : itemsNotInTag) {
                LOGGER.warn("Climate target not in tag: {}", entry.toString());
            }
        }
    }
}
