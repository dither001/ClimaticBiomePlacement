package jaredbgreat.climaticbiome.util;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A gradient noise generator.  This is based on Perlin noise  but 
 * with a few modifications. First, this does not use linear interpolation,
 * but instead scales influence based on Euclidean distance (techincally the
 * square of the distance, because it looked better).  Second, it doesn't 
 * use unit vectors for gradients but instead allows for variable magnitudes 
 * (since when was real terrain limited to consistently have one base slope 
 * everywhere?).  It seems to produce good results and (surprisingly) runs 
 * slightly faster than true Perlin noise.
 * 
 * @author Jared Blackburn
 */
public class HeightNoise {
    int size, interval, currentInterval, regx, regy;
    double[][] field;
    double scale, divisor;
    SpatialNoise random;

    
    public HeightNoise(SpatialNoise random, int size, int interval, 
                double scale, int regx, int regy) {
        this.size = size; 
        this.interval = interval; 
        this.scale = scale;
        this.regx = regx;
        this.regy = regy;
        this.size = size;
        this.random = random;
    }
    
    /**
     * This produce full fractal noise for the parameters specified in the 
     * constructor.  It receives a parameter representing the starting Z value 
     * in the spatial hash function used to generate random values; this is 
     * essentially the series number of the noise map, allowing multiple 
     * unique maps to be generated with the same instance which can be used 
     * to represent different quantities in the same area at the same scale.
     * 
     * @param startz
     * @return 
     */
    public double[][] process(int startz)  {
        field = new double[size][size];
        currentInterval = interval;
        divisor = 1.0;
        while(currentInterval > 2) {
            processOne(startz);            
            divisor *= 2;
            currentInterval /= 2;
            startz += 2;
        }
        return field;
    }
    
    /**
     * This processes of the points for one level of scaling.  Calling this 
     * multiple times at different scales allows for fractal noise to be 
     * generated.
     * 
     * @param startz 
     */
    private void processOne(int startz) {
        int nodesX = size / currentInterval + 1;
        int nodesY = size / currentInterval + 1;
        Vec2D[][] nodes = new Vec2D[nodesX][nodesY];
        for(int i = 0; i < nodesX; i++)
            for(int j = 0; j < nodesY; j++) {
                nodes[i][j] = new Vec2D(random, (regx * nodesX - 1) + i, 
                        (regy * nodesY - 1) + j, startz);
        }
        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++) {
                field[i][j] += processPoint(nodes, i, j) * scale;
        }
    }
    
    /**
     * Calculates the value at a given point.
     * 
     * This adds together the values relative to the gradients at each corner 
     * of the cell, then divides to produce a value in the desired range of 
     * -1 to 1.
     * 
     * @param nodes
     * @param x
     * @param y
     * @return 
     */
    public double processPoint(Vec2D[][] nodes, int x, int y) {
        double out = 0.0;
        
        double ci = (double)currentInterval;        
        double dx = fullFade(x % currentInterval);
        double dy = fullFade(y % currentInterval);
        int    px = (int)(x / currentInterval);
        int    py = (int)(y / currentInterval);        
        
        out += calcLoc(nodes[px][py], 
                    new Vec2D(dx, dy), ci);
        out += calcLoc(nodes[px + 1][py], 
                    new Vec2D((ci - dx), dy), ci);
        out += calcLoc(nodes[px + 1][py + 1], 
                    new Vec2D((ci - dx), (ci - dy)), ci);
        out += calcLoc(nodes[px][py + 1], 
                    new Vec2D(dx, (ci - dy)), ci);        
        
        out /= interval;
        out /= 2.0;
        
        return out;
    }
    
    /**
     * This calculates the noise value at a given point relative to one 
     * specific gradient / origin.  This is one of the core difference 
     * from Perlin noise, as it is base on Euclidian distance rather than 
     * using linear interpolation across on each axis.  The effectively 
     * results in noise that is based on true geometric distance rather than 
     * a "Manhattan" distance.
     * 
     * Technically this uses the square of the distance, not for any analytic 
     * reason but rather for a more artistic one -- testing showed that this 
     * simply looks better (at least to me).
     * 
     * @param from The origin of the gradient
     * @param at the point for which the value is being calculated
     * @param ci "Current Interval" the distance across a cell
     * @return The height relative to the specified gradient
     */
    private double calcLoc(Vec2D from, Vec2D at, double ci) {
        double dx = at.x / ci;
        double dy = at.y / ci;
        double l = (1 - ((dx * dx) + (dy * dy)));
        if(l > 0) {
            return Vec2D.dot(from, at) * l;            
        }        
        return 0.0;
    }
    
    /**
     * This is Ken Perlin's fade function.  It smooths out the values near 
     * the origin and termination point, thus removing grid-like artifact 
     * from the noise map.
     * 
     * @param in
     * @return 
     */
    private double fade(double in) {
        return in * in * in * (in * (in * 6 - 15) + 10);  
    }
    
    /**
     * This performs a scaled version of the fade function in which the range 
     * is transformed to be treat as exactly 1.0.  This is required to make 
     * the function work properly on non-unit lengths.
     * 
     * @param in
     * @return 
     */
    private double fullFade(double in) {
        return fade(in / currentInterval) * currentInterval;
    }
    
    
}
