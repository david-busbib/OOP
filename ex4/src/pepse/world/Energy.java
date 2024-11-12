package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

/**
 * The Energy class represents the energy level in the game.
 * It extends the GameObject class provided by the danogl library.
 */
public class Energy extends GameObject {

    /** The initial energy level. */
    private static final int INITIAL_ENERGY = 100;
    public static final String PORCANTAGE = "%";

    /** The text renderable for displaying the energy level. */
    private TextRenderable textRenderable;

    /** The current energy level. */
    private float energy;

    /**
     * Constructs a new Energy instance.
     *
     * @param topLeft        The position of the energy display, in window coordinates (pixels).
     *                       Note that (0,0) is the top-left corner of the window.
     * @param dimensions     The width and height of the energy display in window coordinates.
     * @param textRenderable The text renderable representing the energy display.
     */
    public Energy(Vector2 topLeft, Vector2 dimensions, TextRenderable textRenderable) {
        super(topLeft, dimensions, textRenderable);
        this.energy = INITIAL_ENERGY;
        this.textRenderable = textRenderable;
        this.textRenderable.setString(energy + PORCANTAGE);
    }

    /**
     * Updates the energy level and refreshes the text display.
     *
     * @param value The new energy value.
     */
    public void updateEnergy(float value) {
        this.energy = value;
        this.textRenderable.setString(energy + PORCANTAGE);
    }
}
