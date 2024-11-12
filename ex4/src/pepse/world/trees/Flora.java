package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;
import utils.ColorSupplier;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;


/**
 * The Flora class represents the flora (trees, leaves, and fruits) in the game.
 */
public class Flora {

    /** The tag for identifying trees. */
    public static final String TAG_FOR_TREE = "tree";

    /** The factor for random leaf placement. */
    public static final int FACTOR_FOR_RANDOM_LEAF = 4;

    /** The beginning angle for leaf rotation. */
    public static final float BEGINNING_ANGLE = 0.f;

    /** The ending angle for leaf rotation. */
    public static final float ENDING_ANGLE = 360.f;

    /** The transition time to change leaf angle. */
    public static final int TRANSITION_TIME_CHANGE_ANGLE = 10;

    /** The size of fruits. */
    public static final int FRUIT_SIZE = Leaf.SIZE - 2;

    /** The wait time for leaf task. */
    public static final double WAIT_TIME_LEAF_TASK = 0.08;

    /** The transition time to change leaf width. */
    public static final double TRANSITION_TIME_CHANGE_WIDTH = 0.1;
    public static final String LEAF_TAG = "leaf";
    public static final String FRUIT_TAG = "fruit";

    /** Function to get ground height at specific x-coordinate. */
    private static Function<Float, Float> groundHeightAt = null;

    /** List to store leaf game objects. */
    private List<GameObject> leafsList;

    /** List to store fruit game objects. */
    private List<GameObject> fruitsList;
    private float time=0;

    private boolean isJumping = false;

    /**
     * Constructor for Flora class.
     *
     * @param groundHeightAt Function to get ground height at specific x-coordinate.
     */
    public Flora(Function<Float, Float> groundHeightAt) {
        Flora.groundHeightAt = groundHeightAt;
    }

    /**
     * Method to set special mode for leaf, fruit, and tree objects.
     *
     * @param isJumped Boolean indicating if special mode is active.
     */
    public void isJumping(boolean isJumped) {
        if(isJumping!=isJumped) {
            Leaf.setSpecialMode(isJumped);
            Fruit.setSpecialMode(isJumped);
            Tree.setSpecialMode(isJumped);
            isJumping = isJumped;
        }
    }

    /**
     * Method to create trees within a specific range.
     *
     * @param minX Minimum x-coordinate for tree placement.
     * @param maxX Maximum x-coordinate for tree placement.
     * @return List of created trees.
     */
    public List<Tree> createInRange(int minX, int maxX) {
        List<Tree> list = new ArrayList<>();
        for (int i = minX; i < maxX; i += Block.SIZE) {
            boolean random = isPlace(Tree.PROBABILITY_OF_TREE);
            if (random) {
                RectangleRenderable renderable = new RectangleRenderable(ColorSupplier.approximateColor
                        (Tree.BASIC_TREE));
                Tree b = new Tree(new Vector2(i, groundHeightAt.apply((float) i)-Tree.HEIGHT_OF_TREE),
                        new Vector2(Block.SIZE, Tree.HEIGHT_OF_TREE), renderable);
                b.setTag(TAG_FOR_TREE);
                list.add(b);
            }
        }
        return list;
    }

    /**
     * Method to create leaf and fruit game objects within a specific range.
     *
     * @param headTreeX X-coordinate of the tree's head.
     * @param headTreeY Y-coordinate of the tree's head.
     * @return List containing leaf and fruit game objects.
     */
    public List<List<GameObject>> createLeafs(float headTreeX, float headTreeY) {
        List<List<GameObject>> list = new ArrayList<>();
        leafsList = new ArrayList<>();
        fruitsList = new ArrayList<>();

        int move_to = FACTOR_FOR_RANDOM_LEAF * Leaf.SIZE;
        time = 0.f;
        for (int i = (int) headTreeX - move_to; i < headTreeX + move_to; i += Leaf.SIZE) {
            for (int j = (int) headTreeY - move_to; j < headTreeY + move_to; j += Leaf.SIZE) {
                if (isPlace(Leaf.PROBABILITY_OF_LEAF))
                    createLeaf(leafsList, i, j);
                else if (isPlace(Fruit.PROBABILITY_FRUIT)) {
                    createFruit(fruitsList, i, j);
                }
            }
        }
        list.add(leafsList);
        list.add(fruitsList);
        return list;
    }

    /**
     * Method to create a leaf game object.
     *
     * @param list List to store leaf game objects.
     * @param i X-coordinate of the leaf.
     * @param j Y-coordinate of the leaf.
     */
    private void createLeaf(List<GameObject> list, int i, int j) {
        RectangleRenderable renderable = new RectangleRenderable(ColorSupplier.approximateColor
                (Leaf.BASIC_LEAF));
        Leaf leaf = new Leaf(new Vector2(i, j), new Vector2(Leaf.SIZE, Leaf.SIZE), renderable);
        leaf.setTag(LEAF_TAG);
        list.add(leaf);
        ScheduledTask scheduledTask = new ScheduledTask(leaf, time += (float) WAIT_TIME_LEAF_TASK,
                false, () -> {
            changeWidth(leaf);
            changeAngle(leaf);
        });
    }

    /**
     * Method to create a fruit game object.
     *
     * @param list List to store fruit game objects.
     * @param i X-coordinate of the fruit.
     * @param j Y-coordinate of the fruit.
     */
    private void createFruit(List<GameObject> list, int i, int j) {
        OvalRenderable renderable = new OvalRenderable(ColorSupplier.approximateColor(Color.red));
        Fruit fruit = new Fruit(new Vector2(i, j), new Vector2(FRUIT_SIZE, FRUIT_SIZE), renderable);
        fruit.setTag(FRUIT_TAG);
        list.add(fruit);
    }

    /**
     * Method to change the angle of a leaf game object.
     *
     * @param leaf The leaf game object.
     */
    private static void changeAngle(Leaf leaf) {
        Transition<Float> angleTransition = new Transition<>(leaf,
                angle -> leaf.renderer().setRenderableAngle(angle),
                BEGINNING_ANGLE, ENDING_ANGLE, Transition.CUBIC_INTERPOLATOR_FLOAT,
                (float) TRANSITION_TIME_CHANGE_ANGLE, Transition.TransitionType.TRANSITION_LOOP,
                null);
    }

    /**
     * Method to change the width of a leaf game object.
     *
     * @param leaf The leaf game object.
     */
    private static void changeWidth(Leaf leaf) {
        Transition<Float> dimensionsTransition = new Transition<>(leaf,
                angle -> leaf.transform().setTopLeftCornerX(angle),
                leaf.getTopLeftCorner().x(), leaf.getTopLeftCorner().x() + 1 / 2.f,
                Transition.CUBIC_INTERPOLATOR_FLOAT, (float) TRANSITION_TIME_CHANGE_WIDTH,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /**
     * Method to check if a place should be filled based on given probability.
     *
     * @param probability The probability of filling the place.
     * @return True if the place should be filled, false otherwise.
     */
    private static boolean isPlace(double probability) {
        float randomValue = (float) new Random().nextDouble();
        return randomValue < probability;
    }

}
