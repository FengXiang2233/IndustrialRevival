package org.irmc.industrialrevival.core;

import java.sql.SQLException;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.irmc.industrialrevival.api.IndustrialRevivalAddon;
import org.irmc.industrialrevival.core.command.IRCommandGenerator;
import org.irmc.industrialrevival.core.data.IDataManager;
import org.irmc.industrialrevival.core.data.MysqlDataManager;
import org.irmc.industrialrevival.core.data.SqliteDataManager;
import org.irmc.industrialrevival.core.implemention.groups.IRItemGroups;
import org.irmc.industrialrevival.core.implemention.items.IRItems;
import org.irmc.industrialrevival.core.listeners.DropListener;
import org.irmc.industrialrevival.core.listeners.GuideListener;
import org.irmc.industrialrevival.core.listeners.ItemHandlerListener;
import org.irmc.industrialrevival.core.listeners.MachineMenuListener;
import org.irmc.industrialrevival.core.message.LanguageManager;
import org.irmc.industrialrevival.core.services.BlockDataService;
import org.irmc.industrialrevival.core.services.IRRegistry;
import org.irmc.industrialrevival.core.services.ItemTextureService;
import org.irmc.industrialrevival.core.utils.FileUtil;
import org.jetbrains.annotations.NotNull;

public final class IndustrialRevival extends JavaPlugin implements IndustrialRevivalAddon {
    @Getter
    private static IndustrialRevival instance;

    @Getter
    private IRRegistry registry;

    @Getter
    private LanguageManager languageManager;

    @Getter
    private IDataManager dataManager;

    @Getter
    private ItemTextureService itemTextureService;

    private @Getter BlockDataService blockDataService;

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
        IRCommandGenerator.registerCommand(this);
    }

    @Override
    public void onEnable() {
        instance = this;

        FileUtil.completeFile(this, "config.yml");

        languageManager = new LanguageManager(this);
        registry = new IRRegistry();

        setupDataManager();

        setupIndustrialRevivalItems();

        setupServices();
        setupListeners();
    }

    private void setupIndustrialRevivalItems() {
        IRItems.setup();
        IRItemGroups.setup();
    }

    private void setupServices() {
        blockDataService = new BlockDataService();
        itemTextureService = new ItemTextureService();
        blockDataService = new BlockDataService();
    }

    private void setupListeners() {
        new MachineMenuListener().register();
        new ItemHandlerListener().register();
        new GuideListener().register();
        new DropListener().register();
    }

    private void setupDataManager() {
        FileConfiguration config = getConfig();
        String storageType = config.getString("storage.type", "sqlite");
        if (storageType.equalsIgnoreCase("sqlite")) {
            try {
                dataManager = new SqliteDataManager();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                dataManager = new MysqlDataManager();
            } catch (Exception e) {
                getLogger().severe("Failed to connect to MySQL database, falling back to SQLite");
                try {
                    dataManager = new SqliteDataManager();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        dataManager.close();
    }

    @Override
    public @NotNull JavaPlugin getPlugin() {
        return this;
    }

    @Override
    public String getIssueTrackerURL() {
        return "https://github.com/IndustrialRevival/IndustrialRevival/issues";
    }
}
