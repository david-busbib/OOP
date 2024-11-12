package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import utils.ColorSupplier;

import java.awt.*;

/**
 * The Fruit class represents a fruit object in the game.
 */
public class Fruit extends GameObject {

    /** The probability of a fruit being created. */
    public static final double PROBABILITY_FRUIT = 0.08;
    /** The wait time*/
    public static final float WAIT_TIME = 0.f;

    /** Flag indicating special mode. */
    private static boolean specialMode = false;

    /** Flag indicating the color of the fruit. */
    private boolean isRed = true;

    /** Renderable for the fruit in orange color. */
    private Renderable newRend = new OvalRenderable(ColorSupplier.approximateColor(Color.orange));

    /** Renderable for the fruit in red color. */
    private Renderable newRend1 = new OvalRenderable(ColorSupplier.approximateColor(Color.red));

    /**
     * Constructs a new Fruit instance.
     *
     * @param topLeftCorner Position of the fruit, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the fruit. Can be null, in which case
     *                      the fruit will not be rendered.
     */
    public Fruit(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    /**
     * Sets the special mode for the fruit.
     *
     * @param specialMode Boolean indicating whether special mode is active or not.
     */
    public static void setSpecialMode(boolean specialMode) {
        Fruit.specialMode = specialMode;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // If special mode is active, alternate between red and orange color for the fruit
        if (specialMode) {
            Renderable rend = chooseRend();
            changeFruitColor(this,rend);
        }
    }

    /**
     * help mto change the fruit color
     * @param this_
     * @param rend
     */
    private static void changeFruitColor(GameObject this_,Renderable rend) {
        this_.renderer().setRenderable(rend);
    }

    /**
     * Chooses the renderable for the fruit based on its current color.
     *
     * @return The renderable for the fruit.
     */
    private Renderable chooseRend() {
        if (isRed) {
            isRed = false;
            return newRend;
        } else {
            isRed = true;
            return newRend1;
        }
    }
}
