import json

def main():
    
    tfc_saplings = [
        "acacia",
        "ash",
        "aspen",
        "birch",
        "blackwood",
        "chestnut",
        "douglas_fir",
        "hickory",
        "kapok",
        "maple",
        "oak",
        "palm",
        "pine",
        "rosewood",
        "sequoia",
        "spruce",
        "sycamore",
        "white_cedar",
        "willow"
    ]

    afc_saplings = [
        "cypress",
        "tualang",
        "hevea",
        "teak",
        "eucalyptus",
        "baobab",
        "fig",
        "mahogany",
        "ironwood",
        "ipe",
        "gum_arabic",
        "acacia_koa",
        "mpingo_blackwood",
        "mountain_fir",
        "balsam_fir",
        "scrub_hickory",
        "bigleaf_maple",
        "weeping_maple",
        "black_oak",
        "live_oak",
        "stone_pine",
        "red_pine",
        "tamarack",
        "giant_rosewood",
        "coast_spruce",
        "sitka_spruce",
        "black_spruce",
        "atlas_cedar",
        "weeping_willow",
        "rainbow_eucalyptus",
        "mountain_ash",
        "weeping_cypress",
        "redcedar",
        "bald_cypress",
        "rubber_fig",
        "small_leaf_mahogany",
        "sapele_mahogany",
        "red_silk_cotton",
        "coast_redwood",
        "poplar",
        "iroko_teak",
        "flame_of_the_forest",
        "lebombo_ironwood",
        "horsetail_ironwood",
        "jaggery_palm"
    ]

    tree_climate_values = {
        "acacia": {
            "rainfall_range": [90, 275],
            "temperature_range": [13.4, 40.0]
        },
        "gum_arabic": {
            "rainfall_range": [90, 275],
            "temperature_range": [11.6, 40.0]
        },
        "acacia_koa": {
            "rainfall_range": [350, 500],
            "temperature_range": [15.3, 24.4]
        },
        "ash": {
            "rainfall_range": [60, 240],
            "temperature_range": [-13.9, 0.7]
        },
        "aspen": {
            "rainfall_range": [350, 500],
            "temperature_range": [-15.7, -1.1]
        },
        "birch": {
            "rainfall_range": [125, 310],
            "temperature_range": [-13.9, 2.5]
        },
        "blackwood": {
            "rainfall_range": [35, 150],
            "temperature_range": [9.8, 20.7]
        },
        "mpingo_blackwood": {
            "rainfall_range": [85, 285],
            "temperature_range": [13.4, 22.5]
        },
        "chestnut": {
            "rainfall_range": [150, 300],
            "temperature_range": [-6.6, 11.6]
        },
        "douglas_fir": {
            "rainfall_range": [305, 500],
            "temperature_range": [-10.3, 6.1]
        },
        "mountain_fir": {
            "rainfall_range": [220, 345],
            "temperature_range": [-1.1, 11.6]
        },
        "balsam_fir": {
            "rainfall_range": [210, 500],
            "temperature_range": [-13.9, -1.1]
        },
        "hickory": {
            "rainfall_range": [210, 400],
            "temperature_range": [-6.6, 11.6]
        },
        "scrub_hickory": {
            "rainfall_range": [400, 475],
            "temperature_range": [11.6, 17.1]
        },
        "kapok": {
            "rainfall_range": [360, 500],
            "temperature_range": [13.4, 24.4]
        },
        "maple": {
            "rainfall_range": [260, 360],
            "temperature_range": [-8.4, 8.0]
        },
        "bigleaf_maple": {
            "rainfall_range": [405, 500],
            "temperature_range": [-4.8, 11.6]
        },
        "weeping_maple": {
            "rainfall_range": [240, 320],
            "temperature_range": [0.7, 4.3]
        },
        "live_oak": {
            "rainfall_range": [390, 500],
            "temperature_range": [0.7, 13.4]
        },
        "black_oak": {
            "rainfall_range": [150, 330],
            "temperature_range": [11.6, 20.7]
        },
        "oak": {
            "rainfall_range": [210, 320],
            "temperature_range": [-8.4, 6.1]
        },
        "palm": {
            "rainfall_range": [0, 250],
            "temperature_range": [17.1, 40.0]
        },
        "pine": {
            "rainfall_range": [60, 270],
            "temperature_range": [-19.4, -3.0]
        },
        "stone_pine": {
            "rainfall_range": [140, 290],
            "temperature_range": [2.5, 18.9]
        },
        "red_pine": {
            "rainfall_range": [185, 320],
            "temperature_range": [-8.4, 4.3]
        },
        "tamarack": {
            "rainfall_range": [150, 500],
            "temperature_range": [-17.5, -1.1]
        },
        "rosewood": {
            "rainfall_range": [245, 360],
            "temperature_range": [15.3, 24.4]
        },
        "giant_rosewood": {
            "rainfall_range": [340, 440],
            "temperature_range": [15.3, 22.5]
        },
        "coast_redwood": {
            "rainfall_range": [320, 500],
            "temperature_range": [4.3, 9.8]
        },
        "sequoia": {
            "rainfall_range": [320, 500],
            "temperature_range": [0.7, 8.0]
        },
        "spruce": {
            "rainfall_range": [330, 500],
            "temperature_range": [-19.4, 2.5]
        },
        "coast_spruce": {
            "rainfall_range": [320, 390],
            "temperature_range": [-12.1, 6.1]
        },
        "sitka_spruce": {
            "rainfall_range": [370, 500],
            "temperature_range": [2.5, 8.0]
        },
        "black_spruce": {
            "rainfall_range": [220, 360],
            "temperature_range": [-17.5, -1.1]
        },
        "sycamore": {
            "rainfall_range": [330, 480],
            "temperature_range": [-6.6, 11.6]
        },
        "white_cedar": {
            "rainfall_range": [100, 220],
            "temperature_range": [-13.9, 4.3]
        },
        "atlas_cedar": {
            "rainfall_range": [165, 500],
            "temperature_range": [8.0, 13.4]
        },
        "willow": {
            "rainfall_range": [330, 500],
            "temperature_range": [-6.6, 9.8]
        },
        "weeping_willow": {
            "rainfall_range": [355, 500],
            "temperature_range": [9.8, 17.1]
        },
        "rainbow_eucalyptus": {
            "rainfall_range": [300, 450],
            "temperature_range": [15.3, 24.4]
        },
        "eucalyptus": {
            "rainfall_range": [170, 325],
            "temperature_range": [8.0, 18.9]
        },
        "mountain_ash": {
            "rainfall_range": [390, 500],
            "temperature_range": [9.8, 18.9]
        },
        "baobab": {
            "rainfall_range": [30, 190],
            "temperature_range": [15.3, 26.2]
        },
        "hevea": {
            "rainfall_range": [390, 500],
            "temperature_range": [17.1, 26.2]
        },
        "mahogany": {
            "rainfall_range": [300, 430],
            "temperature_range": [15.3, 26.2]
        },
        "small_leaf_mahogany": {
            "rainfall_range": [320, 500],
            "temperature_range": [11.6, 18.9]
        },
        "sapele_mahogany": {
            "rainfall_range": [330, 500],
            "temperature_range": [13.4, 26.2]
        },
        "tualang": {
            "rainfall_range": [360, 500],
            "temperature_range": [20.7, 40.0]
        },
        "teak": {
            "rainfall_range": [215, 330],
            "temperature_range": [13.4, 26.2]
        },
        "cypress": {
            "rainfall_range": [100, 260],
            "temperature_range": [2.5, 15.3]
        },
        "weeping_cypress": {
            "rainfall_range": [290, 415],
            "temperature_range": [-8.4, 4.3]
        },
        "redcedar": {
            "rainfall_range": [410, 500],
            "temperature_range": [-4.8, 9.8]
        },
        "bald_cypress": {
            "rainfall_range": [360, 500],
            "temperature_range": [-1.1, 15.3]
        },
        "fig": {
            "rainfall_range": [340, 500],
            "temperature_range": [18.9, 26.2]
        },
        "rubber_fig": {
            "rainfall_range": [290, 400],
            "temperature_range": [9.8, 20.7]
        },
        "red_silk_cotton": {
            "rainfall_range": [250, 350],
            "temperature_range": [20.7, 40.0]
        },
        "ipe": {
            "rainfall_range": [340, 500],
            "temperature_range": [11.6, 20.7]
        },
        "ironwood": {
            "rainfall_range": [320, 430],
            "temperature_range": [20.7, 40.0]
        },
        "lebombo_ironwood": {
            "rainfall_range": [150, 230],
            "temperature_range": [20.7, 40.0]
        },
        "horsetail_ironwood": {
            "rainfall_range": [340, 500],
            "temperature_range": [9.8, 20.7]
        },
        "iroko_teak": {
            "rainfall_range": [210, 320],
            "temperature_range": [17.1, 40.0]
        },
        "flame_of_the_forest": {
            "rainfall_range": [340, 500],
            "temperature_range": [13.4, 24.4]
        },
        "jaggery_palm": {
            "rainfall_range": [400, 500],
            "temperature_range": [20.7, 26.2]
        },
        "poplar": {
            "rainfall_range": [170, 310],
            "temperature_range": [-15.7, -6.6]
        }
    }

    lithistrees_saplings = {
        "replace": False,
        "values": []
    }

    # Tag List Creation

    for tree in tfc_saplings:
        lithistrees_saplings["values"].append(f"tfc:wood/sapling/{tree}")
    for tree in afc_saplings:
        lithistrees_saplings["values"].append(f"afc:wood/sapling/{tree}")

    with open("src/main/resources/data/lithictrees/tags/blocks/saplings.json", 'w') as f:
        f.write(json.dumps(lithistrees_saplings, indent=2))

    # Climate Data Creation

    for k, v in tree_climate_values.items():

        new_trees_dict = {}

        if k in tfc_saplings:
            
            id = f"tfc:wood/sapling/{k}"
            new_trees_dict["tree"] = {}
            new_trees_dict["tree"]["id"] = id

            for x, y in v.items():
                new_trees_dict["tree"][x] = y

        elif k in afc_saplings:
            
            id = f"afc:wood/sapling/{k}"
            new_trees_dict["tree"] = {}
            new_trees_dict["tree"]["id"] = id

            for x, y in v.items():
                new_trees_dict["tree"][x] = y

        else:
            raise(NameError)

        with open(f"src/main/resources/data/lithictrees/tree_climates/{k}.json", 'w') as f:
            f.write(json.dumps(new_trees_dict, indent=4))

    # for k, v in new_trees_dict.items():

    #     json_out = {"__comment__": "Not generated by MCResources",k:v}
    #     with open(f"src/main/resources/data/lithictrees/tree_climates/{v["id"].split('/')[2]}.json", 'w') as f:
    #         f.write(json.dumps(json_out, indent=4))

if __name__ == "__main__":
    main()