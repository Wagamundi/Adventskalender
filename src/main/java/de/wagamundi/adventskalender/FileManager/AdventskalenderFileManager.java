package de.wagamundi.adventskalender.FileManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import net.kyori.adventure.util.Index;
import org.bukkit.Bukkit;
import org.bukkit.ServerTickManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AdventskalenderFileManager {

    private static final Gson gson = new Gson();

    public static int[] readAdventskalenderFromFile(JavaPlugin plugin, String playerName) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        File playerDirectory = new File(plugin.getDataFolder(), "Players/");
        if (!playerDirectory.exists()) {
            playerDirectory.mkdirs();
        }

        File playerFile = new File(playerDirectory, playerName + ".json");

        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
                initializeFile(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        try {
            Object playerFileRaw = new JSONParser().parse(new FileReader(playerFile));
            JSONObject finPlayerFile = (JSONObject) playerFileRaw;

            String finalJsonReturn = finPlayerFile.get("Tuerchen").toString();
            
            return gson.fromJson(finalJsonReturn, int[].class);

        } catch (IOException e) {
            System.out.println("Fehler beim Lesen der Datei: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Fehler beim Parsen der JSON-Daten: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.out.println("Fehler beim Parsen der JSON-Daten in ein Java-Objekt: " + e.getMessage());
        }

        return null;
    }

    private static final Gson gson2 = new Gson();

    public static String[] readAdventskalenderConfig(JavaPlugin plugin, Integer index) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        File configFile = new File(plugin.getDataFolder(), "Config.json");

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                initializeFile2(configFile);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        try {
            Object playerFileRaw = new JSONParser().parse(new FileReader(configFile));
            JSONObject finConfigFile = (JSONObject) playerFileRaw;

            if (index == 0) {
                JSONObject configs = (JSONObject) finConfigFile.get("Configs");
                String year = configs.get("Start-Year").toString();
                String month = configs.get("Start-Month").toString();
                String day = configs.get("Start-Day").toString();

                String[] ar = {year, month, day };

                return ar;
            } else if (index > 0) {
                JSONObject capital = (JSONObject) finConfigFile.get("Tuerchen");
                JSONObject turchen = (JSONObject) capital.get(String.valueOf(index));
                String item = turchen.get("item").toString();
                String count = turchen.get("number").toString();

                String[] ar = {item, count};

                return ar;
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Lesen der Datei: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Fehler beim Parsen der JSON-Daten: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.out.println("Fehler beim Parsen der JSON-Daten in ein Java-Objekt: " + e.getMessage());
        }

        return null;
    }

    public static void saveArrayToPlayerFile(JavaPlugin plugin, int[] array, String playerName) {
        File playerDirectory = new File(plugin.getDataFolder(), "Players/");
        File playerFile = new File(playerDirectory, playerName + ".json");

        JsonArray jsonArray = new JsonArray();
        for (int num : array) {
            jsonArray.add(num);
        }

        try (FileWriter writer = new FileWriter(playerFile)) {
            writer.write("{\"Tuerchen\":" + jsonArray.toString() + "}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void initializeFile(File playerFile) {
        try (FileWriter writer = new FileWriter(playerFile)) {
            JSONObject initialData = new JSONObject();
            initialData.put("Tuerchen", new JSONArray());

            writer.write(initialData.toJSONString());
        } catch (IOException e) {
            System.out.println("Fehler beim Initialisieren der Datei: " + e.getMessage());
        }
    }

    private static void initializeFile2(File configFile) {
        try (FileWriter writer = new FileWriter(configFile)) {
            JSONObject configs = new JSONObject();
            configs.put("Start-Day", 1);
            configs.put("Start-Month", 12);
            configs.put("Start-Year", 2024);

            JSONObject tuerchen = new JSONObject();

            for (int i = 1; i <= 24; i++) {
                JSONObject tuerchenEntry = new JSONObject();
                tuerchenEntry.put("item", "BREAD");
                tuerchenEntry.put("number", "32");

                tuerchen.put(i, tuerchenEntry);
            }

            JSONObject initialData = new JSONObject();
            initialData.put("Configs", configs);
            initialData.put("Tuerchen", tuerchen);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(initialData);

            writer.write(prettyJson);
            writer.flush();

        } catch (IOException e) {
            System.out.println("Fehler beim Initialisieren der Datei: " + e.getMessage());
        }
    }
}
