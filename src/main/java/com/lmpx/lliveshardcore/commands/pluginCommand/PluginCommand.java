package com.lmpx.lliveshardcore.commands.pluginCommand;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import com.lmpx.lliveshardcore.Functions;
import com.lmpx.lliveshardcore.Main;
import com.lmpx.lliveshardcore.commands.LCommand;
import com.lmpx.lliveshardcore.commands.LTabCompleter;
import com.lmpx.lliveshardcore.commands.SubCommand;
import com.lmpx.lliveshardcore.commands.pluginCommand.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

// ...

public class PluginCommand implements CommandExecutor, LCommand {

    public Main plugin = Main.getPlugin(Main.class);
    public List<SubCommand> scs;

    public PluginCommand() {
        scs = new ArrayList<>();

        scs.add(new Reload());
        scs.add(new SetLives());
        scs.add(new SetPoints());
        scs.add(new SetAdvsc());
        scs.add(new SetBoughtLives());

        plugin.getLogger().info(Functions.subcmdregistered(name()));
    }

    public void register() {
        plugin.getCommand(name()).setExecutor(this);
        plugin.getCommand(name()).setTabCompleter(new LTabCompleter(scs));
        plugin.getLogger().info(Functions.cmdregistered(name()));
    }

    @Override
    public String getPermission() {
        return "pluginCommand";
    }

    @Override
    public String name() {
        return "llh";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender.hasPermission(Functions.permAll()) || sender.hasPermission(Functions.permRoot() + getPermission()))) {
            Functions.noPermission(sender);
            return true;
        }

        if (args.length == 0) {
            StringBuilder help = new StringBuilder();
            help.append("&1/" + name() + " &2[ ");
            Iterator<SubCommand> sci = scs.iterator();
            int iterated = 0;
            while (sci.hasNext()) {
                if (iterated < scs.size()-1) {
                    help.append("&3" + sci.next().name() + " &4| ");
                } else {
                    help.append("&3" + sci.next().name());
                }
                iterated++;
            }
            help.append("&2 ]");
            String helpString = help.toString()
                    .replaceAll("&1", "<SOLID:6efff8>")
                    .replaceAll("&2", "<SOLID:ff6e6e>")
                    .replaceAll("&3", "<SOLID:a1ff6e>")
                    .replaceAll("&4", "<SOLID:a3a3a3>");

            sender.sendMessage(IridiumColorAPI.process(helpString));
            return true;
        } else {

            List<String> newargs = new ArrayList<>();
            newargs.addAll(Arrays.asList(args));
            newargs.remove(0);

            Iterator<SubCommand> sci = scs.iterator();

            boolean executed = false;

            while (sci.hasNext() && !executed) {
                SubCommand sc = sci.next();

                String[] aliases = sc.aliases();
                boolean isAlias = false;
                for (int i = 0; i < aliases.length; i++) {
                    String alias = aliases[i];
                    if (args[0].equalsIgnoreCase(alias)) {
                        isAlias = true;
                    }
                }
                if (args[0].equalsIgnoreCase(sc.name()) || isAlias) {
                    try {
                        sc.onCommand(sender, newargs.toArray(new String[newargs.size()]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    executed = true;
                }
            }
            if (!executed) {
                Functions.invalidSubcommand(sender);
            }
        }

        return true;
    }


}
