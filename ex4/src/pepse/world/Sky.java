package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The Sky class represents the sky background in the game.
 */
public class Sky {

    /** The basic color of the sky. */
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    /** The tag for identifying the sky GameObject. */
    public static final String TAG_FOR_SKY = "Sky";

    /**
     * Creates a new sky GameObject.
     *
     * @param windowDimensions The dimensions of the game window.
     * @return The sky GameObject.
     */
    public static GameObject create(Vector2 windowDimensions) {
        GameObject sky = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(TAG_FOR_SKY);
        return sky;
    }
}
