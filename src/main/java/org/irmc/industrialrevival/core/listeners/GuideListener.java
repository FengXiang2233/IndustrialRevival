package org.irmc.industrialrevival.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.irmc.industrialrevival.core.guide.impl.CheatGuideImplementation;
import org.irmc.industrialrevival.core.guide.impl.SurvivalGuideImplementation;
import org.irmc.industrialrevival.core.utils.Constants;
import org.irmc.pigeonlib.pdc.PersistentDataAPI;

public class GuideListener extends AbstractIRListener {
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            int type = PersistentDataAPI.getInt(item.getItemMeta(), Constants.GUIDE_ITEM_KEY, -1);
            if (type == 1) {
                SurvivalGuideImplementation.INSTANCE.open(e.getPlayer());
                e.setCancelled(true);
            } else if (type == 2) {
                CheatGuideImplementation.INSTANCE.open(e.getPlayer());
                e.setCancelled(true);
            }
        }
    }
}
