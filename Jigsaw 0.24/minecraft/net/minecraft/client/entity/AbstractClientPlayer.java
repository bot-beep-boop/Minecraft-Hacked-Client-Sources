package net.minecraft.client.entity;

import java.io.File;

import com.mojang.authlib.GameProfile;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.src.CapeUtils;
import net.minecraft.src.Config;
import net.minecraft.src.PlayerConfigurations;
import net.minecraft.src.Reflector;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

public abstract class AbstractClientPlayer extends EntityPlayer {
	private NetworkPlayerInfo playerInfo;
	private ResourceLocation locationOfCape = null;
	private String nameClear = null;
	private static final String __OBFID = "CL_00000935";

	public AbstractClientPlayer(World worldIn, GameProfile playerProfile) {
		super(worldIn, playerProfile);
		this.nameClear = playerProfile.getName();

		if (this.nameClear != null && !this.nameClear.isEmpty()) {
			this.nameClear = StringUtils.stripControlCodes(this.nameClear);
		}

		CapeUtils.downloadCape(this);
		PlayerConfigurations.getPlayerConfiguration(this);
	}

	/**
	 * Returns true if the player is in spectator mode.
	 */
	public boolean isSpectator() {
		NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler()
				.getPlayerInfo(this.getGameProfile().getId());
		return networkplayerinfo != null && networkplayerinfo.getGameType() == WorldSettings.GameType.SPECTATOR;
	}

	/**
	 * Checks if this instance of AbstractClientPlayer has any associated player
	 * data.
	 */
	public boolean hasPlayerInfo() {
		return this.getPlayerInfo() != null;
	}

	protected NetworkPlayerInfo getPlayerInfo() {
		if (this.playerInfo == null) {
			this.playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getUniqueID());
		}

		return this.playerInfo;
	}

	/**
	 * Returns true if the player has an associated skin.
	 */
	public boolean hasSkin() {
		NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
		return networkplayerinfo != null && networkplayerinfo.hasLocationSkin();
	}

	/**
	 * Returns true if the player instance has an associated skin.
	 */
	public ResourceLocation getLocationSkin() {
		// TODO Jigsaw skinprotect
		if (Jigsaw.getModuleByName("SkinProtect").isToggled()) {
			return DefaultPlayerSkin.getDefaultSkin(this.getUniqueID());
		}
		NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
		return networkplayerinfo == null ? DefaultPlayerSkin.getDefaultSkin(this.getUniqueID())
				: networkplayerinfo.getLocationSkin();
	}

	public ResourceLocation getLocationCape() {
		if (!Config.isShowCapes()) {
			return null;
		} else if (this.locationOfCape != null) {
			return this.locationOfCape;
		} else {
			NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
			return networkplayerinfo == null ? null : networkplayerinfo.getLocationCape();
		}
	}

	public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
		TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
		Object object = texturemanager.getTexture(resourceLocationIn);

		if (object == null) {
			object = new ThreadDownloadImageData((File) null,
					String.format("http://skins.minecraft.net/MinecraftSkins/%s.png",
							new Object[] { StringUtils.stripControlCodes(username) }),
					DefaultPlayerSkin.getDefaultSkin(getOfflineUUID(username)), new ImageBufferDownload());
			texturemanager.loadTexture(resourceLocationIn, (ITextureObject) object);
		}

		return (ThreadDownloadImageData) object;
	}

	/**
	 * Returns true if the username has an associated skin.
	 */
	public static ResourceLocation getLocationSkin(String username) {
		return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
	}

	public String getSkinType() {
		NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
		return networkplayerinfo == null ? DefaultPlayerSkin.getSkinType(this.getUniqueID())
				: networkplayerinfo.getSkinType();
	}

	public float getFovModifier() {
		float f = 1.0F;

		if (this.capabilities.isFlying) {
			f *= 1.1F;
		}

		IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
		f = (float) ((double) f
				* ((iattributeinstance.getAttributeValue() / (double) this.capabilities.getWalkSpeed() + 1.0D) / 2.0D));

		if (this.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f)) {
			f = 1.0F;
		}

		if (this.isUsingItem() && this.getItemInUse().getItem() == Items.bow) {
			int i = this.getItemInUseDuration();
			float f1 = (float) i / 20.0F;

			if (f1 > 1.0F) {
				f1 = 1.0F;
			} else {
				f1 = f1 * f1;
			}

			f *= 1.0F - f1 * 0.15F;
		}

		return Reflector.ForgeHooksClient_getOffsetFOV.exists()
				? Reflector.callFloat(Reflector.ForgeHooksClient_getOffsetFOV, new Object[] { this, Float.valueOf(f) })
				: f;
	}

	public String getNameClear() {
		return this.nameClear;
	}

	public ResourceLocation getLocationOfCape() {
		return this.locationOfCape;
	}

	public void setLocationOfCape(ResourceLocation p_setLocationOfCape_1_) {
		this.locationOfCape = p_setLocationOfCape_1_;
	}
}
