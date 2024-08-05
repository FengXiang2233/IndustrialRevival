package org.irmc.industrialrevival.core.utils;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.irmc.industrialrevival.api.menu.MachineMenu;
import org.irmc.industrialrevival.api.menu.MachineMenuPreset;
import org.irmc.industrialrevival.api.objects.DataType;
import org.irmc.industrialrevival.api.objects.IRBlockData;
import org.irmc.industrialrevival.core.IndustrialRevival;

public class DataUtil {
    public static IRBlockData getBlockData(Location location) {
        return IndustrialRevival.getInstance().getBlockDataService().getBlockData(location);
    }
    public static void setData(Location location, String key, String value) {
        getBlockData(location).setData(key, DataType.STRING, value);
    }

    public static String getData(Location location, String key) {
        return getBlockData(location).getData(key, DataType.STRING);
    }

    public static boolean hasData(Location location, String key) {
        return getBlockData(location).getData(key, DataType.STRING) != null;
    }

    public static void setBlockData(Location location, IRBlockData blockData) {
        IndustrialRevival.getInstance().getBlockDataService().getBlockDataMap().put(location, blockData);
    }

    public static boolean hasBlockData(Location location) {
        return getBlockData(location) != null;
    }

    public static IRBlockData removeBlockData(Location location) {
        return IndustrialRevival.getInstance().getBlockDataService().getBlockDataMap().remove(location);
    }

    public static void removeData(Location location, String key) {
        // TODO: Implement
    }

    public static MachineMenu getMachineMenu(Location location) {
        return getBlockData(location).getMachineMenu();
    }

    public static MachineMenu getMachineMenu(Block block) {
        return getBlockData(block.getLocation()).getMachineMenu();
    }
    
    public static boolean hasMachineMenu(Location location) {
        return getBlockData(location).getMachineMenu() != null;
    }
    
    public static boolean hasMachineMenu(Block block) {
        return hasMachineMenu(block.getLocation());
    }

    public static MachineMenuPreset getMachineMenuPresetById(String id) {
        return IndustrialRevival.getInstance().getRegistry().getMenuPresets().get(id);
    }

    public static boolean isBlock(Location location, String id) {
        return getBlockData(location).getId().equals(id);
    }

    public static void setPDC(PersistentDataHolder holder, NamespacedKey key, String value) {
        holder.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
    }

    public static String getPDC(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    public static void removePDC(PersistentDataHolder holder, NamespacedKey key) {
        holder.getPersistentDataContainer().remove(key);
    }

    public static boolean hasPDC(PersistentDataHolder holder, NamespacedKey key) {
        return holder.getPersistentDataContainer().has(key);
    }

    public static boolean hasPDC(PersistentDataHolder holder, NamespacedKey key, String value) {
        return holder.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }
}
