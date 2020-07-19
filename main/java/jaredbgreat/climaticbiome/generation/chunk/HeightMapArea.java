package jaredbgreat.climaticbiome.generation.chunk;

import jaredbgreat.climaticbiome.generation.cache.AbstractWeaklyCacheable;
import jaredbgreat.climaticbiome.generation.cache.WeakCache;
import jaredbgreat.climaticbiome.util.HeightNoiseMap;
import jaredbgreat.climaticbiome.util.ModMath;
import jaredbgreat.climaticbiome.util.SpatialHash;

public class HeightMapArea extends AbstractWeaklyCacheable {
	public static final int CSIZE = 8;          // Width and depth in chunks
	public static final int BSIZE = CSIZE * 16; // Width and depth in blocks
	public static final int SCALE = 128;         // Multiplier for height
	
	private final float[][] heightMap;
	
	
	public HeightMapArea(int x, int z, SpatialHash rand, HeightMapManager manager) {
		super(x, z, manager.getCache());
		//System.out.println("x = " + x + ", z = " + z);
    	HeightNoiseMap noise = new HeightNoiseMap(BSIZE, BSIZE, BSIZE, SCALE);
    	heightMap = noise.process(rand, x * BSIZE, z * BSIZE);
	}
	
	
	public int[] getChunkHieghts(int x, int z, float[] biomeData) {
    	int[] out = new int[256];
    	int startx = ModMath.modRight(x, CSIZE) * 16;
    	int startz = ModMath.modRight(z, CSIZE) * 16;
    	int endx   = startx + 16;
    	int endz   = startz + 16;
    	int ix = 0, jz = 0;
    	int index;
    	for(int i = startx; i < endx; i++, ix++) {
    		jz = 0;
    		for(int j = startz; j < endz; j++, jz++) {
    			index = (ix * 16) + jz;
    			out[index] = (int)((heightMap[i][j] * biomeData[index + 256]) 
    					+ (biomeData[index] * 20) + 68);
    		}
    	}
    	return out;
	}
	
	
	
	

}