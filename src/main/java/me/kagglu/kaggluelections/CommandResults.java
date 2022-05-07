package me.kagglu.kaggluelections;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandResults implements CommandExecutor {
    private Main instance;

    public CommandResults(Main instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = Bukkit.getPlayer(sender.getName());
        if (p == null) {
            return false;
        }
        if (instance.allowVoting && !(p.isOp())) {
            p.sendMessage("§4§lResults can't be checked during the voting period!");
            return true;
        }
        if (instance.candidates.size() == 0) {
            p.sendMessage("§2§lNo current election results.");
        }
        int[] votesCopy = new int[instance.candidates.size()];
        String[] namesCopy = new String[instance.candidates.size()];
        for (int i = 0; i < instance.candidates.size(); i++) {
            votesCopy[i] = instance.candidates.get(i).getVoteCount();
            namesCopy[i] = instance.candidates.get(i).getName();
        }
        for (int i = 0; i < votesCopy.length; i++) {
            int maxJ = i;
            for (int j = i; j < votesCopy.length; j++) {
                if (votesCopy[j] > votesCopy[maxJ]) {
                    maxJ = j;
                }
            }
            int tempVotes = votesCopy[i];
            String tempName = namesCopy[i];
            votesCopy[i] = votesCopy[maxJ];
            namesCopy[i] = namesCopy[maxJ];
            votesCopy[maxJ] = tempVotes;
            namesCopy[maxJ] = tempName;
        }
        p.sendMessage("§2§lElection results:");
        for (int i = 0; i < votesCopy.length; i++) {
            if (votesCopy[i] == 1) {
                p.sendMessage("§2" + (i + 1) + ". " + namesCopy[i] + ": §6" + votesCopy[i] + " vote");
            } else {
                p.sendMessage("§2" + (i + 1) + ". " + namesCopy[i] + ": §6" + votesCopy[i] + " votes");
            }
        }
        return true;
    }
}
