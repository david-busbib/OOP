package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The Sun class represents the sun object in the game.
 */
public class Sun {

    /** The factor by which to scale the sun dimensions. */
    private static final float SUN_DIMENSIONS_FACTOR = 2;

    /** The radius of the sun along the Y-axis. */
    private static final float RADIUS_Y = 10;

    /** The initial position of the sun. */
    public static final Vector2 FIRST_PLACE_OF_THE_SUN = new Vector2(1, 1);

    /** The size of the sun. */
    public static final Vector2 SUN_SIZE = new Vector2(90, 90);

    /** The initial angle of the sun. */
    public static final float FIRST_ANGLE = 0.f;

    /** The final angle of the sun. */
    public static final float LAST_ANGLE = 360.f;

    /** The factor determining the position of the sun center during the day-night cycle. */
    public static final float FACTOR_SUN_CENTER = 2 / 3.f;

    /**
     * Creates a sun game object with specified window dimensions and cycle length.
     *
     * @param windowDimensions The dimensions of the window.
     * @param cycleLength      The length of the day-night cycle.
     * @return The created sun game object.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength) {
        // Calculate the initial position of the sun
        Vector2 initializedSun = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);

        // Create an oval renderable for the sun with yellow color
        OvalRenderable ovalRenderable = new OvalRenderable(Color.YELLOW);

        // Create a sun game object with initial position, size, and oval renderable
        GameObject sun = new GameObject(FIRST_PLACE_OF_THE_SUN, SUN_SIZE, ovalRenderable);

        // Set coordinate space to camera coordinates
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        // Calculate the center position of the sun during the day-night cycle
        Vector2 cycleCenter = new Vector2(windowDimensions.x() / 2,
                windowDimensions.y() * FACTOR_SUN_CENTER + 50);

        // Create a transition for rotating the sun during the day-night cycle
        Transition<Float> transition = new Transition<>(sun,
                angle -> sun.setCenter(initializedSun.subtract(
                        cycleCenter).rotated(angle).add(cycleCenter)),
                FIRST_ANGLE,
                LAST_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);

        // Assign a tag to identify the sun object
        sun.setTag("sun");

        // Return the created sun object
        return sun;
    }
}
