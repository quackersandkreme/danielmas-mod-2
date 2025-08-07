package net.nightium;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.nightium.command.SwapInvCommand;
import net.nightium.core.SwapManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwapInventory implements ModInitializer {
	public static final String MOD_ID = "swapinventory";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			SwapInvCommand.register(dispatcher);
		});

		ServerTickEvents.END_SERVER_TICK.register(SwapManager::onServerTick);
		LOGGER.info("SwapInventory mod initialized!");
	}
}