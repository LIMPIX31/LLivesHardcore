package com.lmpx.lliveshardcore.versions;


import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSUtils_1_16_R3 implements NMSUtils {

    @Override
    public void sendActionBar(Player player, String jsonFString) {
        PacketPlayOutTitle packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, IChatBaseComponent.ChatSerializer.a(jsonFString));
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
}
