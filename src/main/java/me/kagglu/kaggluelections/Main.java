package me.kagglu.kaggluelections;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public ArrayList<String> voters = new ArrayList<>();
    public ArrayList<Candidate> candidates = new ArrayList<>();
    public PluginManager pluginManager;
    Boolean allowVoting;
    String question;

    File voterRolls;
    File electionResults;
    File other;

    @Override
    public void onEnable() {
        this.saveConfig();
        voterRolls = new File("plugins/KaggluElections/voterRolls.txt");
        electionResults = new File("plugins/KaggluElections/electionResults.txt");
        other = new File("plugins/KaggluElections/other.txt");
        try {
            if (voterRolls.createNewFile() && electionResults.createNewFile() && other.createNewFile()) {
                allowVoting = false;
            }
        } catch (Exception e) {
            getServer().getLogger().info("KaggluElections: error creating files");
        }
        initialize();
        pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new Listener(this), this);
        this.getCommand("vote").setExecutor(new CommandVote(this));
        this.getCommand("results").setExecutor(new CommandResults(this));
        this.getCommand("election").setExecutor(new CommandElection(this));
    }

    @Override
    public void onDisable() {
        writeFile();
    }

    public void initialize() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(voterRolls));
            String line;
            while ((line = reader.readLine()) != null) {
                voters.add(line);
            }
            reader.close();

            BufferedReader reader2 = new BufferedReader(new FileReader(electionResults));
            String line2;
            while ((line2 = reader2.readLine()) != null) {
                try {
                    candidates.add(new Candidate(line2.substring(0, line2.indexOf(' ')), Integer.parseInt((line2.substring(line2.indexOf(' ') + 1)))));
                } catch (Exception e) {
                    getServer().getLogger().info("KaggluElections: array error occurred while reading file: "+ e.getLocalizedMessage());
                    getServer().getLogger().info("line: " + line2);
                }
            }
            reader2.close();

            BufferedReader reader3 = new BufferedReader(new FileReader(other));
            String line3 = reader3.readLine();
            allowVoting = Boolean.parseBoolean(line3);
            question = reader3.readLine();
            reader3.close();
        } catch (Exception e) {
            getServer().getLogger().info("KaggluElections: Error while reading file");
            getServer().getLogger().info(e.getLocalizedMessage());
        }
    }

    public void writeFile() {
        try {
            FileWriter writer = new FileWriter(voterRolls);
            for (int i = 0; i < voters.size(); i++) {
                writer.write(voters.get(i) + "\n");
            }
            writer.close();

            FileWriter writer2 = new FileWriter(electionResults);
            for (int i = 0; i < candidates.size(); i++) {
                writer2.write(candidates.get(i).getName() + " " + candidates.get(i).getVoteCount() + "\n");
            }
            writer2.close();

            FileWriter writer3 = new FileWriter(other);
            writer3.write(allowVoting.toString() + '\n');
            writer3.write(question);
            writer3.close();
        } catch (Exception e) {
            getServer().getLogger().info("DailyReward: Error while writing file");
        }
    }
}
