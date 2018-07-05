package jaredbgreat.climaticbiome.generation.chunk.bopbiomes;

import net.minecraft.world.biome.Biome;
import jaredbgreat.climaticbiome.biomes.ModBiomes;
import jaredbgreat.climaticbiome.generation.chunk.ChunkTile;
import jaredbgreat.climaticbiome.generation.chunk.biomes.IBiomeSpecifier;

public class GetBoPDesert implements IBiomeSpecifier {
	private static int lush, oasis, outback, wasteland, xeric;

	@Override
	public int getBiome(ChunkTile tile) {
		return reallyGetBiome(tile, tile.getBiomeSeed() % 8);
	}
	
	
	private int reallyGetBiome(ChunkTile tile, int in) {
		switch(in % 8) {
		case 0:
		case 1:
			return lush;
		case 2:
		case 3:
			return outback;
		case 4:
		case 5:
			return xeric;
		case 6:
			return oasis;
		case 7:
			if((tile.getBiomeSeed() % 3) == 0) {
				return wasteland;
			} else {
				return reallyGetBiome(tile, tile.getBiomeSeed() % 7);
			}
		default:
			return xeric;
		}
	}
	
	
	public static void init() {
		//FIXME: How do I do this?
		//lush       = Biome.getIdForBiome(???);
		
	}
	
	
	

}