package me.planetguy.remaininmotion.client ;

import org.lwjgl.opengl.GL11;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.spectre.MotiveSpectreEntity;
import me.planetguy.remaininmotion.spectre.RotativeSpectreEntity;

public class RotativeSpectreRenderer extends RIMTileEntityRenderer
{
	
	public static final int[][] matrices=new int[][] {
		{ 0,-1, 0},
		{ 0, 1, 0},
		{ 0, 0,-1},
		{ 0, 0, 1},
		{-1, 0, 0},
		{ 1, 0, 0},
	};
	
	@Override
	public void renderTileEntityAt ( net . minecraft . tileentity . TileEntity TileEntity , double X , double Y , double Z , float PartialTick ) {
		Render . PushMatrix ( ) ;
		
		try {

			RotativeSpectreEntity Spectre = ( RotativeSpectreEntity ) TileEntity ;

			if ( Spectre . RenderCacheKey != null )
			{
				double Offset ;

				if ( Configuration . CarriageMotion . RenderInFinalPositionDuringLag 
						&& ( Spectre . TicksExisted >= Configuration . CarriageMotion . MotionDuration ) )
				{
					Offset = 1 ;
				}
				else
				{
					Offset = Spectre . Velocity * ( Spectre . TicksExisted + PartialTick ) ;
				}
				if(Spectre != null && Spectre.RenderCacheKey != null) {

					Render.Translate(X, Y, Z); //negative player pos

					Render . Translate ( 0.5,0.5,0.5 );

					int axis=Spectre.getAxis();

					Render . Rotate ( Offset * -90, matrices[axis][0], matrices[axis][1],  matrices[axis][2] ) ; //TODO implement other angles

					Render . Translate ( -Spectre.xCoord-.5, -Spectre.yCoord-.5, -Spectre.zCoord-.5); //negative block pos
					
					Integer DisplayList = CarriageRenderCache . lookupDisplayList ( Spectre . RenderCacheKey ) ;

					if ( DisplayList != null )
					{
						Render . ResetBoundTexture ( ) ;

						Render . ExecuteDisplayList ( DisplayList ) ;

						Render . ResetBoundTexture ( ) ;
					}

				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		Render . PopMatrix ( ) ;
	}
	@Override
	public void Render ( net . minecraft . tileentity . TileEntity TileEntity , float PartialTick )
	{
	}
}
