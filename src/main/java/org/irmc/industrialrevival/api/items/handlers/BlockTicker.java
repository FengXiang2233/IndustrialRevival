package org.irmc.industrialrevival.api.items.handlers;

import org.bukkit.block.Block;
import org.irmc.industrialrevival.api.items.IndustrialRevivalItem;
import org.irmc.industrialrevival.api.menu.MachineMenu;
import org.irmc.industrialrevival.api.objects.IRBlockData;
import org.irmc.industrialrevival.api.objects.exceptions.IncompatibleItemHandlerException;
import org.irmc.pigeonlib.items.ItemUtils;

@FunctionalInterface
public interface BlockTicker extends ItemHandler {
    void onTick(Block block, MachineMenu menu, IRBlockData data);

    @Override
    default IncompatibleItemHandlerException isCompatible(IndustrialRevivalItem item) {
        if (!ItemUtils.isActualBlock(item.getItem().getType())) {
            return new IncompatibleItemHandlerException("Only actual blocks can be ticked", item.getId());
        }
        return null;
    }
}
