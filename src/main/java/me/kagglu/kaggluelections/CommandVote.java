package me.kagglu.kaggluelections;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommandVote implements CommandExecutor {
    private Main instance;
    public SimpleDateFormat format = new SimpleDateFormat("dd");

    public CommandVote(Main instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String day = format.format((new Date()));
        Player p = Bukkit.getPlayer(sender.getName());
        if (p == null) {
            return false;
        }
        if (args.length == 0) {
            displayCandidates(p);
            return true;
        }
        if (!(instance.allowVoting)) {
            p.sendMessage("§4§lVoting is not currently open!");
            return true;
        }
        for (int i = 0; i < instance.voters.size(); i++) {
            if (instance.voters.get(i).equals(p.getName())) {
                p.sendMessage("§4§lYou can't vote twice!");
                return true;
            }
        }
        for (int i = 0; i < instance.candidates.size(); i++) {
            if (args[0].equalsIgnoreCase(instance.candidates.get(i).getName())) {
                instance.candidates.get(i).setVoteCount(instance.candidates.get(i).getVoteCount() + 1); //increment vote count
                instance.voters.add(p.getName());
                instance.writeFile();
                p.sendMessage("§2§lVote successfully cast for " + instance.candidates.get(i).getName() + "!");
                return true;
            }
        }
        p.sendMessage("§4§lInvalid name!");
        return true;
    }

    public void displayCandidates(Player p) {
        p.sendMessage("§4§lVoting multiple times on different accounts is not allowed! One vote per person, not account.");
        p.sendMessage("§2§l");
        p.sendMessage(" §b§l------ELECTION OPTIONS------");
        for (int i = 0; i < instance.candidates.size(); i++) {
            p.sendMessage(instance.candidates.get(i).getName());
        }
        p.sendMessage("§b§l--------------------------------");
        if (!instance.question.equalsIgnoreCase("")) {
            p.sendMessage("§2§lElection question: " + instance.question);
        }
    }
}
