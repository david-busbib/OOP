package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;

/**
 * The Leaf class represents a leaf object in the game.
 */
public class Leaf extends GameObject {
    /** The FIRST angle for the leaf to move */
    public static final float FIRST_ANGLE = 0.f;
    /** the angle the leaf need to arrive */
    public static final float SECOND_ANGLE = 90.f;
    /** The basic color of the leaf. */
    public static Color BASIC_LEAF = new Color(50, 200, 30);

    /** The size of the leaf. */
    public static final int SIZE = (int) (Block.SIZE * 0.75);

    /** The probability of a leaf being created. */
    public static final double PROBABILITY_OF_LEAF = 0.6;

    /** Flag indicating special mode. */
    private static boolean specialMode = false;

    /**
     * Constructs a new Leaf instance.
     *
     * @param topLeftCorner Position of the leaf, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the leaf. Can be null, in which case
     *                      the leaf will not be rendered.
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        // Configure physics properties of the leaf
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // If special mode is active, rotate the leaf
        if (specialMode) {
            Transition<Float> angleTransition = new Transition<>(this,
                    angle -> this.renderer().setRenderableAngle(angle),
                    FIRST_ANGLE, SECOND_ANGLE, Transition.CUBIC_INTERPOLATOR_FLOAT,
                    (float) 1, Transition.TransitionType.TRANSITION_ONCE, null);

        }
    }

    /**
     * Sets the special mode for the leaf.
     *
     * @param specialMode Boolean indicating whether special mode is active or not.
     */
    public static void setSpecialMode(boolean specialMode) {
        Leaf.specialMode = specialMode;
    }
}
