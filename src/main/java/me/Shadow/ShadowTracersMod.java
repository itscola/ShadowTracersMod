package me.Shadow;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

@Mod(modid = ShadowTracersMod.MODID, version = ShadowTracersMod.VERSION)
public class ShadowTracersMod
{
    public static final String MODID = "shadowtracersmod";
    public static final String VERSION = "1.0.0";
    
    KeyBinding tracers;
    boolean toggled = false;
    static ArrayList<String> players = new ArrayList<>();
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        
        tracers = new KeyBinding("Tracers", Keyboard.KEY_B, "Shadow's Tracers Mod");
        ClientRegistry.registerKeyBinding(tracers);
        
        ClientCommandHandler.instance.registerCommand(new TracersIgnoreCommand());
        ClientCommandHandler.instance.registerCommand(new DisplayIgnoredNamesCommand());
    }
    
    @SubscribeEvent
    public void onKey(KeyInputEvent event)
    {
    	if(tracers.isPressed())
    	{
    		toggled = !toggled;
    		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "Tracers set to " + EnumChatFormatting.YELLOW + toggled));
    	}
    }
    
    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event)
    {
    	Minecraft mc = Minecraft.getMinecraft();
    	World world = mc.theWorld;
    	EntityPlayerSP player = mc.thePlayer;
    	
    	if(player != null && world != null && toggled)
    	{
    		List<EntityPlayer> playerList = world.playerEntities;
    		for(EntityPlayer otherPlayer : playerList)
    		{
    			if(!players.contains(otherPlayer.getName().toLowerCase()) && !otherPlayer.getName().equals(player.getName()))
    			{
    				Color color;
    				if(otherPlayer.isInvisible())
    					color = new Color(153, 153, 153);
    				else
    				{
    					color = new Color(255, Math.min(255, (int)(player.getDistanceToEntity(otherPlayer)*5)), 0);
    				}
    				draw3DLine(player.getPositionEyes(player.getEyeHeight()), otherPlayer.getPositionEyes(otherPlayer.getEyeHeight()), color, event.partialTicks);
    			}
    		}
    	}
    }
    
    public void draw3DLine(Vec3 pos1, Vec3 pos2, Color colour, float partialTicks)
    {
		Minecraft mc = Minecraft.getMinecraft();
		World world = mc.theWorld;
		EntityPlayerSP player = mc.thePlayer;
		
		Entity render = Minecraft.getMinecraft().getRenderViewEntity();
		WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
		
		double realX = render.lastTickPosX + (render.posX - render.lastTickPosX) * partialTicks;
		double realY = render.lastTickPosY + (render.posY - render.lastTickPosY) * partialTicks;
		double realZ = render.lastTickPosZ + (render.posZ - render.lastTickPosZ) * partialTicks;
		
		GlStateManager.pushMatrix();
		//GlStateManager.translate(-realX, -realY, -realZ);
		GlStateManager.translate(-player.posX, -player.posY, -player.posZ); //tester code
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GL11.glLineWidth(2);
		GlStateManager.color(colour.getRed() / 255f, colour.getGreen() / 255f, colour.getBlue()/ 255f, colour.getAlpha() / 255f);
		worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
		
		
		Vec3 vec = player.getPositionVector(); //start: tester code
		double mx = vec.xCoord;
		double mz = vec.zCoord;
		double my = vec.yCoord + player.getEyeHeight();
		double drawBeforeCameraDist = 1.0; //distance from camera to tracer start
		double pitch = ((player.rotationPitch + 90) * Math.PI) / 180;
		double yaw = ((player.rotationYaw + 90) * Math.PI) / 180;
		mx += Math.sin(pitch) * Math.cos(yaw) * drawBeforeCameraDist;
		mz += Math.sin(pitch) * Math.sin(yaw) * drawBeforeCameraDist;
		my += Math.cos(pitch) * drawBeforeCameraDist; //end: tester code

		
		worldRenderer.pos(mx, my, mz).endVertex();
		//worldRenderer.pos(pos1.xCoord, pos1.yCoord, pos1.zCoord).endVertex();
		worldRenderer.pos(pos2.xCoord, pos2.yCoord, pos2.zCoord).endVertex();
		Tessellator.getInstance().draw();

		GlStateManager.translate(realX, realY, realZ);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}
}
