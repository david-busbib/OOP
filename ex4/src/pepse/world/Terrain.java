package pepse.world;

import danogl.util.Vector2;
import danogl.gui.rendering.RectangleRenderable;
import utils.ColorSupplier;
import utils.NoiseGenerator;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * The Terrain class represents the terrain in the game.
 */
public class Terrain {

    /** The factor determining the size of the terrain. */
    public static final float FACTOR_SIZE_OF_TERRAIN = (1 / (float) 3);

    /** The factor used for generating noise to create the terrain. */
    public static final int NOISE_FACTOR = Block.SIZE * 7;

    /** The base color of the ground. */
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);


    public static final String GROUND_TAG = "ground";

    /** The dimensions of the game window. */
    private Vector2 windowDimensions;

    /** The NoiseGenerator instance for generating terrain noise. */
    private final NoiseGenerator noiseGenerator;

    /** The ground height at x = 0. */
    private double groundHeightAtX0;

    /**
     * Constructs a new Terrain instance.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param seed             The seed used for generating terrain noise.
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        this.windowDimensions = windowDimensions;
        this.groundHeightAtX0 = windowDimensions.y() * FACTOR_SIZE_OF_TERRAIN;
        this.noiseGenerator = new NoiseGenerator(seed, (int) groundHeightAtX0);
    }

    /**
     * Calculates the ground height at the given x-coordinate.
     *
     * @param x The x-coordinate.
     * @return The ground height at the given x-coordinate.
     */
    public float groundHeightAt(float x) {
        return windowDimensions.y() - (float) (Math.floor(1.3 * noiseGenerator.noise(x, NOISE_FACTOR) /
                Block.SIZE) * Block.SIZE + groundHeightAtX0);
    }

    /** Function to calculate ground height at any given x-coordinate. */
    public Function<Float, Float> callGround = this::groundHeightAt;

    /**
     * Creates a list of blocks representing the terrain within the specified x-range.
     *
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return A list of blocks representing the terrain.
     */
    public List<Block> createInRange(int minX, int maxX) {
        List<Block> list = new LinkedList<>();
        for (int i = minX; i < maxX; i += Block.SIZE) {
            double max = groundHeightAt(i);
            for (double j = max; j < windowDimensions.y(); j += Block.SIZE) {
                RectangleRenderable renderable = new RectangleRenderable(
                        ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(i, (float) j),
                        Vector2.ONES.mult(Block.SIZE), renderable);
                block.setTag(GROUND_TAG);
                list.add(block);
            }
        }
        return list;
    }
}
