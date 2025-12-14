package elf.calc.farmmod3000.client.modules;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;


import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public class AutoChatManager {


    private static boolean enabled = true;
    private static final Gson GSON = new Gson();
    private static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {}.getType();
    private static Map<String, String> triggers = new HashMap<>();


    public static void setEnabled(boolean value) {
        enabled = value;
    }


    public static boolean handle(String message, MinecraftClient client) {
        if (!enabled) return false;


        String msg = message.toLowerCase();


        for (Map.Entry<String, String> entry : triggers.entrySet()) {
            if (msg.contains(entry.getKey().toLowerCase())) {
                client.player.networkHandler.sendChatMessage(entry.getValue());
                return true;
            }
        }
        return false;
    }


    public static void loadConfig() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve("autochatcommand.json");


        try {
            if (!Files.exists(path)) {
                try (Writer writer = Files.newBufferedWriter(path)) {
                    Map<String, String> def = Map.of("спавн", "/spawn");
                    GSON.toJson(def, writer);
                }
            }


            try (Reader reader = Files.newBufferedReader(path)) {
                triggers = GSON.fromJson(reader, MAP_TYPE);
                if (triggers == null) triggers = new HashMap<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            triggers = new HashMap<>();
        }
    }
}