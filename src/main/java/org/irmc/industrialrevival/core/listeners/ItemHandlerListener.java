package org.irmc.industrialrevival.core.listeners;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.irmc.industrialrevival.api.items.IndustrialRevivalItem;
import org.irmc.industrialrevival.api.items.IndustrialRevivalItemStack;
import org.irmc.industrialrevival.api.items.attributes.InventoryBlock;
import org.irmc.industrialrevival.api.items.attributes.ItemDroppable;
import org.irmc.industrialrevival.api.items.handlers.BlockBreakHandler;
import org.irmc.industrialrevival.api.items.handlers.BlockPlaceHandler;
import org.irmc.industrialrevival.api.items.handlers.BlockUseHandler;
import org.irmc.industrialrevival.api.items.handlers.ToolUseHandler;
import org.irmc.industrialrevival.api.items.handlers.UseItemInteractHandler;
import org.irmc.industrialrevival.api.items.handlers.WeaponUseHandler;
import org.irmc.industrialrevival.api.menu.MachineMenu;
import org.irmc.industrialrevival.api.objects.IRBlockData;
import org.irmc.industrialrevival.implementation.IndustrialRevival;

public class ItemHandlerListener extends AbstractIRListener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (item instanceof IndustrialRevivalItemStack iris) {
            String id = iris.getId();
            IndustrialRevivalItem iritem = IndustrialRevivalItem.getById(id);
            UseItemInteractHandler handler = iritem.getItemHandler(UseItemInteractHandler.class);
            if (handler != null) {
                handler.onInteract(e);
            }
        }
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getItemInHand();
        if (item instanceof IndustrialRevivalItemStack iris) {
            String id = iris.getId();
            IndustrialRevivalItem iritem = IndustrialRevivalItem.getById(id);

            Block block = e.getBlockPlaced();

            IndustrialRevival.getInstance().getItemTextureService().blockPlacing(e);
            IndustrialRevival.getInstance().getDataManager().handleBlockPlacing(block.getLocation(), id);

            BlockPlaceHandler handler = iritem.getItemHandler(BlockPlaceHandler.class);

            if (handler != null) {
                handler.onBlockPlace(player, block, false);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        IRBlockData blockData = IndustrialRevival.getInstance()
                .getBlockDataService()
                .getBlockData(e.getBlock().getLocation());
        if (blockData != null) {
            String id = blockData.getId();
            IndustrialRevivalItem iritem = IndustrialRevivalItem.getById(id);
            Block block = e.getBlock();

            IndustrialRevival.getInstance().getItemTextureService().blockBreaking(e);
            IndustrialRevival.getInstance().getDataManager().handleBlockBreaking(block.getLocation());

            BlockBreakHandler handler = iritem.getItemHandler(BlockBreakHandler.class);
            if (handler != null) {
                handler.onBlockBreak(player, block, false);
            }

            e.setDropItems(false);
            if (!(iritem instanceof ItemDroppable)) {
                World world = block.getWorld();
                world.dropItemNaturally(block.getLocation(), iritem.getItem().clone());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockUse(PlayerInteractEvent e) {
        if (e.getAction().isRightClick()) {
            Block block = e.getClickedBlock();
            if (block != null) {
                Location location = block.getLocation();
                IRBlockData blockData =
                        IndustrialRevival.getInstance().getBlockDataService().getBlockData(location);
                if (blockData != null) {
                    String id = blockData.getId();
                    IndustrialRevivalItem iritem = IndustrialRevivalItem.getById(id);
                    BlockUseHandler handler = iritem.getItemHandler(BlockUseHandler.class);
                    if (handler != null) {
                        handler.onRightClick(e);
                    }

                    if (iritem instanceof InventoryBlock) {
                        MachineMenu menu = blockData.getMachineMenu();
                        if (menu != null) {
                            menu.open(e.getPlayer());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onToolUse(BlockBreakEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        if (item instanceof IndustrialRevivalItemStack iris) {
            if (!isTool(item)) {
                return;
            }

            String id = iris.getId();
            IndustrialRevivalItem iritem = IndustrialRevivalItem.getById(id);

            ToolUseHandler handler = iritem.getItemHandler(ToolUseHandler.class);
            if (handler != null) {
                handler.onToolUse(e, iris, new ArrayList<>(e.getBlock().getDrops()));
            }
        }
    }

    @EventHandler
    public void onWeaponUse(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player p) {
            ItemStack item = p.getInventory().getItemInMainHand();
            if (item instanceof IndustrialRevivalItemStack iris) {
                String id = iris.getId();
                IndustrialRevivalItem iritem = IndustrialRevivalItem.getById(id);

                Material material = item.getType();
                String str = material.toString();
                if (!str.endsWith("SWORD")) {
                    return;
                }

                WeaponUseHandler handler = iritem.getItemHandler(WeaponUseHandler.class);
                if (handler != null) {
                    handler.onHit(e, p, iris);
                }
            }
        }
    }

    private boolean isSword(ItemStack item) {
        Material material = item.getType();
        return (Tag.ITEMS_SWORDS.isTagged(material));
    }

    private boolean isTool(ItemStack item) {
        Material material = item.getType();
        return (Tag.ITEMS_AXES.isTagged(material)
                || Tag.ITEMS_HOES.isTagged(material)
                || Tag.ITEMS_PICKAXES.isTagged(material)
                || Tag.ITEMS_SHOVELS.isTagged(material)
                || material == Material.SHEARS);
    }
}
