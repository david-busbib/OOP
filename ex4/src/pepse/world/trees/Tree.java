package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import utils.ColorSupplier;

import java.awt.*;
import java.util.Random;

/**
 * The Tree class represents a tree object in the game.
 */
public class Tree extends GameObject {

    /** The probability of a tree being created. */
    public static final double PROBABILITY_OF_TREE = 0.05;

    /** The height of the tree. */
    public static final int HEIGHT_OF_TREE = 130;

    /** The maximum value for the red component of the tree color. */
    public static final int MAX_RED_VALUE = 150;

    /** The minimum value for the red component of the tree color. */
    public static final int MIN_RED_VALUE = 50;

    /** The basic color of the tree. */
    public static Color BASIC_TREE = new Color(100, 50, 20);

    /** Flag indicating special mode. */
    private static boolean specialMode = false;

    /** The x-coordinate of the tree. */
    private final float xCoordinate;

    /** The y-coordinate of the tree. */
    private final float yCoordinate;

    /** Random generator for choosing tree color. */
    private final Random randomColor = new Random();

    /** The green value of the tree color. */
    private final int GREEN_VALUE = 50;

    /** The blue value of the tree color. */
    private final int BLUE_VALUE = 20;

    /**
     * Constructs a new Tree instance.
     *
     * @param topLeftCorner Position of the tree, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the tree. Can be null, in which case
     *                      the tree will not be rendered.
     */
    public Tree(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        // Configure physics properties of the tree
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        // Store the coordinates of the tree
        xCoordinate = topLeftCorner.x();
        yCoordinate = topLeftCorner.y();
    }

    /**
     * Sets the special mode for the tree.
     *
     * @param specialMode Boolean indicating whether special mode is active or not.
     */
    public static void setSpecialMode(boolean specialMode) {
        Tree.specialMode = specialMode;
    }

    /**
     * Gets the x-coordinate of the tree.
     *
     * @return The x-coordinate of the tree.
     */
    public float getxCordinate() {
        return xCoordinate;
    }

    /**
     * Gets the y-coordinate of the tree.
     *
     * @return The y-coordinate of the tree.
     */
    public float getyCordinate() {
        return yCoordinate;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // If special mode is active, change the color of the tree
        if (specialMode) {
            RectangleRenderable rend = chooseRend();
            changeTreeColor(this,rend);
        }
    }
    /**
     * help to change the tree color
     * @param thi
     * @param rend
     */
    private static void changeTreeColor(GameObject thi,Renderable rend) {
        thi.renderer().setRenderable(rend);
    }

    /**
     * Chooses the renderable for the tree, changing its color randomly.
     *
     * @return The chosen renderable for the tree.
     */
    private RectangleRenderable chooseRend() {
        // Generate a random number for the red component of the tree color
        int randomNumber = randomColor.nextInt(MAX_RED_VALUE - MIN_RED_VALUE + 1) + MIN_RED_VALUE;
        // Create a color with the random red value and fixed green and blue values
        Color color = new Color(randomNumber, GREEN_VALUE, BLUE_VALUE);
        // Create and return a rectangle renderable with the chosen color
        return new RectangleRenderable(ColorSupplier.approximateColor(color));
    }
}
