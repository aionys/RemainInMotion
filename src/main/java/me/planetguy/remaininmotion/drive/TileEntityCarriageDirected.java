package me.planetguy.remaininmotion.drive;

import me.planetguy.lib.util.Debug;
import me.planetguy.lib.util.SidedIcons;
import me.planetguy.remaininmotion.motion.CarriageMatchers;
import me.planetguy.remaininmotion.util.transformations.Directions;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RiMItems;
import me.planetguy.remaininmotion.drive.gui.Buttons;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityCarriageDirected extends TileEntityCarriageEngine {

	public Directions	pointedDir=Directions.NegY;
	
	public static SidedIcons helper;

    public TileEntityCarriageDirected() {
        super();
    }
	
	@Override
	public Directions getSignalDirection() {
		if(super.getSignalDirection() != null)
			return pointedDir;
		else
			return null;
	}

	@Override
	public void Setup(EntityPlayer Player, ItemStack Item) {
		super.Setup(Player, Item);
		int l = BlockPistonBase.determineOrientation(Player.worldObj, xCoord, yCoord, zCoord, Player);
		pointedDir = Directions.values()[l].opposite();
	}

	public void HandleNeighbourBlockChange() {
		Stale = false;

		CarriageDirection = null;

		boolean CarriageDirectionValid = true;

		setSignalDirection(null);

		for (Directions Direction : Directions.values()) {
			int X = xCoord + Direction.deltaX;
			int Y = yCoord + Direction.deltaY;
			int Z = zCoord + Direction.deltaZ;

			if (worldObj.isAirBlock(X, Y, Z)) {
				continue;
			}

			if (isSideClosed(Direction.ordinal())) {
				continue;
			}

			net.minecraft.block.Block Id = worldObj.getBlock(X, Y, Z);
			net.minecraft.tileentity.TileEntity te = worldObj.getTileEntity(X, Y, Z);

			Moveable m = CarriageMatchers.getMover(Id, worldObj.getBlockMetadata(X, Y, Z), te);

			if (m != null) {
				if (CarriageDirection != null) {
					CarriageDirectionValid = false;
				} else {
					CarriageDirection = Direction;
				}
			}
			if (Id.isProvidingWeakPower(worldObj, X, Y, Z, Direction.ordinal()) > 0) {
				setSignalDirection(Directions.NegX); //doesn't matter
			}
		}

		if (!CarriageDirectionValid) {
			CarriageDirection = null;
		}

	}
	
	public void WriteCommonRecord(NBTTagCompound TagCompound) {
		super.WriteCommonRecord(TagCompound);
		if(pointedDir != null)
			TagCompound.setByte("pointedDir", (byte) pointedDir.ordinal());
	}
	
	public void ReadCommonRecord(NBTTagCompound TagCompound) {
		super.ReadCommonRecord(TagCompound);
		if(TagCompound.hasKey("pointedDir"))
			pointedDir=Directions.values()[TagCompound.getByte("pointedDir")];
	}
	
	public IIcon getIcon(int side, int meta) {
		try {
			if(SideClosed[side])
				return super.getIcon(side, meta);
			else
				return getIconHelper().getIcon(ForgeDirection.values()[pointedDir.ordinal()], side);
		} catch (Throwable Throwable) {
			 Throwable . printStackTrace ( ) ;

			return (Blocks.iron_block.getIcon(0, 0));
		}
	}
	
	public SidedIcons getIconHelper() {
		return helper;
	}
	
    public void setConfiguration(long flags, EntityPlayerMP changer){
        // make sure nothing fucks with 'flags' before setting the heading
        // add a server side mod to fix a weird error where an arrayindexoutofboundsexception is thrown
        pointedDir=Directions.values()[(int) (flags&7)%6];
    	setConfigurationSuper(flags, changer);
    }

    @Override
    public int getGuiIndex() { return 2; }
}
