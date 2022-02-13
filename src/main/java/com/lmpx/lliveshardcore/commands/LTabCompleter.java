package com.lmpx.lliveshardcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LTabCompleter implements TabCompleter {
  private final List<SubCommand> scs;

  public LTabCompleter(List<SubCommand> scs) {
    this.scs = scs;
  }

  @Nullable
  @Override
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
    switch (args.length) {
      case 0: {
        return null;
      }
      case 1: {
        List<String> tab = new ArrayList<>();
        Iterator<SubCommand> scsi = scs.iterator();
        while (scsi.hasNext()) {
          tab.add(scsi.next().name());
        }
        return tab;
      }
    }
    return null;
  }
}
