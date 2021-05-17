package me.Shadow;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import me.Shadow.ShadowTracersMod;

public class TracersIgnoreCommand extends CommandBase
{
	@Override
	public String getCommandName() {
		return "tracersignore";
	}

	@Override
	public String getCommandUsage(ICommandSender arg0)
	{
		return "/" + getCommandName() + " <playername>";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	@Override
	public void processCommand(ICommandSender arg0, String[] arg1) throws CommandException
	{
		EntityPlayer player = (EntityPlayer)arg0;
		if(arg1.length == 0)
		{
			player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Incorrect Usage! " + getCommandUsage(arg0)));
			return;
		}
		
		if(ShadowTracersMod.players.contains(arg1[0].toLowerCase()))
    	{
			ShadowTracersMod.players.remove(arg1[0].toLowerCase());
    		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Shadow's Tracers Mod: " + EnumChatFormatting.DARK_PURPLE + "Player unignored -> " + EnumChatFormatting.GOLD + arg1[0]));
    	}
    	else
    	{
    		ShadowTracersMod.players.add(arg1[0].toLowerCase());
    		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Shadow's Tracers Mod: " + EnumChatFormatting.DARK_PURPLE + "Player ignored -> " + EnumChatFormatting.GOLD + arg1[0]));
    	}
	}
}
