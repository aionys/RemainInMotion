package me.planetguy.remaininmotion.core ;

import me.planetguy.remaininmotion.BlockBlacklist;
import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.DefaultCarriageMatcher;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.crafting.Recipes;
import me.planetguy.remaininmotion.drive.CarriageTranslocatorEntity;
import me.planetguy.remaininmotion.network.PacketManager;
import me.planetguy.remaininmotion.util.Reflection;
import net.minecraft.item.Item;

public abstract class Core
{
	public static void HandlePreInit ( )
	{
	}

	public static void HandleInit ( )
	{
		Reflection.init();
		
		ModInteraction . Establish ( ) ;

		CreativeTab . Prepare ( ) ;

		RIMBlocks . Initialize ( ) ;

		Items . Initialize ( ) ;

		CreativeTab . Initialize ( Item.getItemFromBlock(RIMBlocks . Carriage ) );
		
		RiMRegistry.registerMatcher(new DefaultCarriageMatcher());
		
		PacketManager.init();
	}

	public static void HandlePostInit ( )
	{
		Recipes . Register ( ) ;

		BlockBlacklist . Initialize ( ) ;
	}

	public static void HandleServerStopping ( )
	{
		try{
			CarriageTranslocatorEntity . ActiveTranslocatorSets . clear ( ) ;
		}catch(Error e){
			//e.printStackTrace();
		}
	}

}
