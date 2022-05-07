package me.kagglu.kaggluelections;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandElection implements CommandExecutor {
    private final Main instance;

    public CommandElection(Main instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = instance.getServer().getPlayer(sender.getName());
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            if (p != null) {
                printHelp(p);
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("addcandidate")) {
            addCandidate(sender, args);
        } else if (args[0].equalsIgnoreCase("removecandidate")) {
            removeCandidate(sender, args);
        } else if (args[0].equalsIgnoreCase("setvotes")) {
            setVotes(sender, args);
        } else if (args[0].equalsIgnoreCase("start")) {
            startOrEndElection(sender, args);
        } else if (args[0].equalsIgnoreCase("end")) {
            startOrEndElection(sender, args);
        } else if (args[0].equalsIgnoreCase("resetvoter")) {
            resetVoter(sender, args);
        } else if (args[0].equalsIgnoreCase("setquestion")) {
            setQuestion(sender, args);
        } else if (args[0].equalsIgnoreCase("resetquestion")) {
            setQuestion(sender, new String[]{"setquestion", ""});
        } else if (args[0].equalsIgnoreCase("resetelection")) {
            resetElection(sender, args);
        } else {
            if (p != null) {
                p.sendMessage("§4§lUnknown command. Type \"/election help\" for help.");
            }
        }

        instance.writeFile();
        return true;
    }

    public void addCandidate(CommandSender sender, String[] args) {
        Player p = instance.getServer().getPlayer(sender.getName());
        if (args.length < 2) {
            if (p != null) {
                p.sendMessage("§4§lInvalid usage! Usage: /election addcandidate <candidate>");
            }
            return;
        }
        boolean exists = false;
        for (int i = 1; i < args.length; i++) {
            for (int j = 0; j < instance.candidates.size(); j++) {
                if (instance.candidates.get(j).getName().equals(args[i])) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                if (p != null) {
                    p.sendMessage("§4§l" + args[i] + " already exists!");
                }
                exists = false;
            } else {
                instance.candidates.add(new Candidate(args[i]));
                if (p != null) {
                    p.sendMessage("§2§lAdded candidate " + args[i]);
                }
            }
        }
    }

    public void removeCandidate(CommandSender sender, String[] args) {
        Player p = instance.getServer().getPlayer(sender.getName());
        if (args.length < 2) {
            if (p != null) {
                p.sendMessage("§4§lInvalid usage!");
            }
            return;
        }
        if (args[1].equalsIgnoreCase("all")) {
            if (instance.candidates.size() > 0) {
                for (int i = instance.candidates.size() - 1; i >= 0; i--) {
                    instance.candidates.remove(i);
                }
            }
            if (p != null) {
                p.sendMessage("§2§lAll candidates removed!");
            }
            return;
        }

        boolean removed = false;
        for (int j = 1; j < args.length; j++) {
            for (int i = 0; i < instance.candidates.size(); i++) {
                if (instance.candidates.get(i).getName().equals(args[j])) {
                    instance.candidates.remove(i);
                    removed = true;
                    break;
                }
            }
            if (p != null) {
                if (removed) {
                    p.sendMessage("§2§lRemoved candidate " + args[j]);
                    removed = false;
                } else {
                    p.sendMessage("§4§lCouldn't find candidate " + args[j]);
                }
            }
        }
    }

    public void setVotes(CommandSender sender, String[] args) {
        Player p = instance.getServer().getPlayer(sender.getName());
        if (args.length < 3) {
            if (p != null) {
                p.sendMessage("§4§lInvalid usage! Usage: /election setvotes <candidate> <amount>");
            }
            return;
        }
        try {
            for (int i = 0; i < instance.candidates.size(); i++) {
                if (instance.candidates.get(i).getName().equalsIgnoreCase(args[1])) {
                    instance.candidates.get(i).setVoteCount(Integer.parseInt(args[2]));
                    if (p != null) {
                        p.sendMessage("§2§lSet vote count of " + args[1] + " to " + args[2]);
                    }
                    return;
                }
            }
        } catch (Exception e) {
            if (p != null) {
                p.sendMessage("§4§lInvalid usage! Usage: /election setvotes <candidate> <amount>");
            }
            return;
        }
        if (p != null) {
            p.sendMessage("§4§lCouldn't find candidate " + args[1]);
        }
    }

    public void startOrEndElection(CommandSender sender, String[] args) {
        if (instance.allowVoting && args[0].equalsIgnoreCase("end")) {
            instance.getServer().broadcastMessage("§4§lVoting is now disabled!");
            instance.allowVoting = false;
        } else if (args[0].equalsIgnoreCase("start")) {
            instance.getServer().broadcastMessage("§2§lVoting is now enabled!");
            instance.allowVoting = true;
        }
    }

    public void resetVoter(CommandSender sender, String[] args) {
        Player p = Bukkit.getPlayer(sender.getName());
        if (args.length < 2) {
            if (p != null) {
                p.sendMessage("§4§lInvalid usage! Usage: /election resetVoter <name>");
            }
            return;
        }
        if (args[1].equalsIgnoreCase("all")) {
            if (instance.voters.size() == 0) {
                return;
            }
            for (int i = instance.voters.size() - 1; i >= 0; i--) {
                instance.voters.remove(i);
            }
            if (p != null) {
                p.sendMessage("§2§lAll voters reset!");
            }
            return;
        }
        boolean removed = false;
        for (int j = 1; j < args.length; j++) {
            for (int i = 0; i < instance.voters.size(); i++) {
                if (instance.voters.get(i).equals(args[1])) {
                    instance.voters.remove(i);
                    removed = true;
                    break;
                }
            }
            if (p != null) {
                if (removed) {
                    p.sendMessage("§2§lVoter " + args[1] + " reset!");
                } else {
                    p.sendMessage("§4§lCouldn't find voter " + args[1] + "! Either they don't exist, or haven't voted.");
                }
            }
        }
    }

    public void setQuestion(CommandSender sender, String[] args) {
        Player p = Bukkit.getPlayer(sender.getName());
        if (args.length < 2) {
            instance.question = "";
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            stringBuilder.append(args[i]);
        }
        instance.question = stringBuilder.toString();
        if (p != null) {
            p.sendMessage("§2§lElection question set to: " + instance.question);
        }
    }

    public void resetElection(CommandSender sender, String[] args) {
        Player p = Bukkit.getPlayer(sender.getName());
        if (args.length < 2) {
            if (p != null) {
                p.sendMessage("§4§lAre you sure you want to completely reset all parts of the election? To confirm, do /election resetelection confirm");
            }
            return;
        }
        if (!args[1].equalsIgnoreCase("confirm")) {
            return;
        }
        if (instance.voters.size() > 0) {
            for (int i = instance.voters.size() - 1; i >= 0; i--) {
                instance.voters.remove(i);
            }
        }
        if (instance.candidates.size() > 0) {
            for (int i = instance.candidates.size() - 1; i >= 0; i--) {
                instance.candidates.remove(i);
            }
        }
        if (p != null) {
            p.sendMessage("§2§lElection completely reset");
        }
        instance.allowVoting = false;
        instance.question = "";
    }

    public void printHelp(Player p) {
        p.sendMessage("§6§lElection commands: (<> denotes mandatory field, [] optional)");
        p.sendMessage("§6/election addcandidate <name> [name]: §2Adds all stated candidates");
        p.sendMessage("§6/election removecandidate <name> [name]: §2Removes every stated candidate (or give name \"all\" to remove all)");
        p.sendMessage("§6/election setvotes <name> <amount>: §2Sets vote count of candidate <name> to <amount>");
        p.sendMessage("§6/election start: §2Starts the election, allowing voting");
        p.sendMessage("§6/election end: §2Ends the election, disallowing voting");
        p.sendMessage("§6/election resetvoter <name> [name]: §2Allows named voters to vote again (or give name \"all\" to reset all)");
        p.sendMessage("§6/election setquestion <question>: §2Sets election question");
        p.sendMessage("§6/election resetquestion: §2Resets election question to nothing");
        p.sendMessage("§6/election resetelection: §2Resets everything (voters, candidates, election question, allow voting status)");
    }
}
