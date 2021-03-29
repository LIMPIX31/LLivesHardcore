package com.lmpx.lliveshardcore.commands;

import com.lmpx.lliveshardcore.Functions;

public interface LCommand {

    String getPermission();

    String name();

    default String getPermissionWithRoot() {
        return Functions.permRoot() + getPermission();
    }

}
