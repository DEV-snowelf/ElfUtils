package elf.calc.farmmod3000.client.modules;


import net.minecraft.client.MinecraftClient;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MathCalcManager {


    private static boolean enabled = true;


    private static final Pattern PLUS_PATTERN =
            Pattern.compile("\\b(\\d+)\\s*\\+\\s*(\\d+)\\b");


    public static void setEnabled(boolean value) {
        enabled = value;
    }


    public static void handle(String message, MinecraftClient client) {
        if (!enabled) return;


        Matcher matcher = PLUS_PATTERN.matcher(message);
        if (matcher.find()) {
            int a = Integer.parseInt(matcher.group(1));
            int b = Integer.parseInt(matcher.group(2));
            client.player.networkHandler.sendChatMessage(String.valueOf(a + b));
        }
    }
}