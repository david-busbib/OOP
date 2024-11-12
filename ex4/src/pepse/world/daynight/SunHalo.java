package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * The SunHalo class represents the halo around the sun in the game.
 */
public class SunHalo {

    /** The color of the sun halo. */
    public static final Color COLOR_OF_THE_SUN_HALO = new Color(255, 255, 0, 20);

    /** The tag for identifying the sun halo. */
    public static final String TAG_FOR_THE_HALO_SUN = "haloSun";

    /** The size of the sun halo. */
    public static final Vector2 SIZE_OF_HALO = new Vector2(130, 130);

    /**
     * Creates a halo around the sun with the specified sun object.
     *
     * @param sun The sun game object.
     * @return The created halo sun game object.
     */
    public static GameObject create(GameObject sun) {
        // Create an oval renderable for the sun halo with the specified color
        OvalRenderable ovalRenderable = new OvalRenderable(COLOR_OF_THE_SUN_HALO);

        // Create a game object for the halo sun with initial position, size, and oval renderable
        GameObject haloSun = new GameObject(Vector2.ONES, SIZE_OF_HALO, ovalRenderable);

        // Set coordinate space to camera coordinates
        haloSun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        // Add a component to update the position of the halo sun based on the sun's position
        haloSun.addComponent(deltaTime -> haloSun.setCenter(sun.getCenter()));

        // Assign a tag to identify the halo sun object
        haloSun.setTag(TAG_FOR_THE_HALO_SUN);

        // Return the created halo sun object
        return haloSun;
    }
}
