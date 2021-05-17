package me.Shadow;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class DisplayIgnoredNamesCommand extends CommandBase
{
	@Override
	public String getCommandName() {
		return "displayignorednames";
	}

	@Override
	public String getCommandUsage(ICommandSender arg0)
	{
		return "/" + getCommandName();
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	@Override
	public void processCommand(ICommandSender arg0, String[] arg1) throws CommandException
	{
		EntityPlayer player = (EntityPlayer)arg0;
		player.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + ShadowTracersMod.players.toString()));
	}
}