package com.wiredtext;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.text.Text;

public class WiredTextMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        // Register /wiredtext command
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                ClientCommandManager.literal("wiredtext")
                    .executes(ctx -> {
                        WiredTextState.active = true;
                        WiredTextState.scheduleNext();
                        ctx.getSource().sendFeedback(
                            Text.literal("§7[WiredText] Активирован. Жди...")
                        );
                        return 1;
                    })
            );

            dispatcher.register(
                ClientCommandManager.literal("stopwiredtext")
                    .executes(ctx -> {
                        WiredTextState.active = false;
                        WiredTextState.nextTriggerTime = 0;
                        WiredTextState.showUntil = 0;
                        ctx.getSource().sendFeedback(
                            Text.literal("§7[WiredText] Остановлен.")
                        );
                        return 1;
                    })
            );
        });

        // Tick every client tick to check if we should trigger
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            WiredTextState.tick();
        });

        // Register HUD overlay
        WiredTextOverlay.register();
    }
}
