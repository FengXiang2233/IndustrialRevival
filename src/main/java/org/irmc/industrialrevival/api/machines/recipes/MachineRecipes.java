package org.irmc.industrialrevival.api.machines.recipes;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Machine recipes are unordered by default.
 */

public class MachineRecipes {
    private @Getter static final Set<MachineRecipe> recipes = new HashSet<>();

    public MachineRecipes() {}

    private static MachineRecipe findNextRecipe(ItemStack... items) {
        return findNextRecipe(List.of(items));
    }

    private static MachineRecipe findNextRecipe(List<ItemStack> items) {
        Map<ItemStack, Integer> itemsMap = new HashMap<>();
        for (ItemStack item : items) {
            itemsMap.put(item, item.getAmount());
        }
        return findNextRecipe(itemsMap);
    }

    private static MachineRecipe findNextRecipe(Map<ItemStack, Integer> items) {
        for (MachineRecipe recipe : recipes) {
            if (recipe.isMatch(items)) {
                return recipe;
            }
        }
        return null;
    }

    public void addRecipe(MachineRecipe recipe) {
        recipes.add(recipe);
    }

    public void addRecipe(int processTime, int energyCost, List<ItemStack> inputs, List<ItemStack> outputs) {
        final Map<ItemStack, Integer> inputsMap = new HashMap<>();
        for (ItemStack input : inputs) {
            inputsMap.put(input, input.getAmount());
        }
        final Map<ItemStack, Integer> outputsMap = new HashMap<>();
        for (ItemStack output : outputs) {
            outputsMap.put(output, output.getAmount());
        }
        recipes.add(new MachineRecipe(processTime, energyCost, inputsMap, outputsMap));
    }
}
