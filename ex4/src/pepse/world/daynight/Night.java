package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import utils.ColorSupplier;

import java.awt.*;

/**
 * The Night class represents a night object in the game.
 */
public class Night {

    /** The opacity value for midnight. */
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /** The initial opacity value. */
    private static final Float IN_OPCITY = 0.f;

    /** The tag for identifying night objects. */
    public static final String NIGHT = "night";

    /**
     * Creates a night game object with specified window dimensions and cycle length.
     *
     * @param windowDimensions The dimensions of the window.
     * @param cycleLength      The length of the day-night cycle.
     * @return The created night game object.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        // Create a rectangle renderable with black color
        RectangleRenderable renderable =
                new RectangleRenderable(ColorSupplier.approximateColor(Color.BLACK));

        // Create a game object with left direction, window dimensions, and rectangle renderable
        GameObject night = new GameObject(Vector2.LEFT, windowDimensions, renderable);

        // Set coordinate space to camera coordinates
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        // Assign the NIGHT tag to the night object
        night.setTag(NIGHT);

        // Create a transition for opacity change over time
        Transition<Float> transition = new Transition<>(night, night.renderer()::setOpaqueness,
                IN_OPCITY, MIDNIGHT_OPACITY, Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength / 2, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        // Return the created night object
        return night;
    }
}
