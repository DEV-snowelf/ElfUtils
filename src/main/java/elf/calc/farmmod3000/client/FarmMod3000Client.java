package elf.calc.farmmod3000.client;

import elf.calc.farmmod3000.client.modules.AutoChatManager;
import elf.calc.farmmod3000.client.modules.MathCalcManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class FarmMod3000Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AutoChatManager.loadConfig();

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;

            String msg = message.getString();

            if (AutoChatManager.handle(msg, client)) return;
            MathCalcManager.handle(msg, client);
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

            dispatcher.register(
                    ClientCommandManager.literal("elfconfig")
                            .then(ClientCommandManager.literal("reload")
                                    .executes(ctx -> {
                                        AutoChatManager.loadConfig();
                                        ctx.getSource().sendFeedback(Text.literal("§a[ElfConfig] Конфиг перезагружен"));
                                        return 1;
                                    }))
            );

            dispatcher.register(
                    ClientCommandManager.literal("elfautochat")
                            .then(ClientCommandManager.literal("true")
                                    .executes(ctx -> {
                                        AutoChatManager.setEnabled(true);
                                        ctx.getSource().sendFeedback(Text.literal("§a[ElfAutoChat] ВКЛЮЧЕН"));
                                        return 1;
                                    }))
                            .then(ClientCommandManager.literal("false")
                                    .executes(ctx -> {
                                        AutoChatManager.setEnabled(false);
                                        ctx.getSource().sendFeedback(Text.literal("§c[ElfAutoChat] ВЫКЛЮЧЕН"));
                                        return 1;
                                    }))
            );

            dispatcher.register(
                    ClientCommandManager.literal("elfcalc")
                            .then(ClientCommandManager.literal("true")
                                    .executes(ctx -> {
                                        MathCalcManager.setEnabled(true);
                                        ctx.getSource().sendFeedback(Text.literal("§a[ElfCalc] Калькулятор ВКЛЮЧЕН"));
                                        return 1;
                                    }))
                            .then(ClientCommandManager.literal("false")
                                    .executes(ctx -> {
                                        MathCalcManager.setEnabled(false);
                                        ctx.getSource().sendFeedback(Text.literal("§c[ElfCalc] Калькулятор ВЫКЛЮЧЕН"));
                                        return 1;
                                    }))
            );
        });
    }
}