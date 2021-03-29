package com.lmpx.lliveshardcore.commands;


import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class SubCommand {

    public abstract void onCommand(CommandSender sender, @NotNull String[] args);

    public abstract String name();
    public abstract String[] aliases();

}
