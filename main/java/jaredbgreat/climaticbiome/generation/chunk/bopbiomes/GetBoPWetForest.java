package jaredbgreat.climaticbiome.generation.chunk.bopbiomes;

import jaredbgreat.climaticbiome.generation.chunk.ChunkTile;
import jaredbgreat.climaticbiome.generation.chunk.EnumBiomeType;
import jaredbgreat.climaticbiome.generation.chunk.biomes.IBiomeSpecifier;

public class GetBoPWetForest implements IBiomeSpecifier {
	private static int rain;

	@Override
	public int getBiome(ChunkTile tile) {
		if((tile.getBiomeSeed() % 4) == 0) {
			return rain;
		} else {
			return EnumBiomeType.BCFOREST.specifier.getBiome(tile);
		}
	}

}