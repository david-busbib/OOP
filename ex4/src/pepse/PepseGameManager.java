package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Tree;
import pepse.world.Terrain;
import pepse.world.Sky;
import pepse.world.Block;
import pepse.world.Avatar;
import pepse.world.Energy;

import java.util.List;
/**
 * The PepseGameManager class manages the main game logic and initialization.
 * It extends the GameManager class from the danogl library.
 */
public class PepseGameManager extends GameManager {

    /** The layer for rendering leaf objects. */
    public static final int LAYER_LEAF = Layer.STATIC_OBJECTS + 20;

    /** The minimum x-coordinate for spawning trees. */
    public static final int MIN_X_TREES = 2;

    /** The dimensions of the energy display. */
    public static final int ENERGY_DIM = 20;

    /** The initial energy value. */
    public static final int INIT_ENERGY = 100;

    /** The initial position of the avatar. */
    public static final int INIT_POS_AVATAR = 0;

    /** The layer for rendering the sun halo. */
    public static final int HALO_SUN_LAYER = Layer.BACKGROUND + 1;

    /** The layer for rendering the sun. */
    public static final int SUN_LAYER = Layer.BACKGROUND + 2;

    /** The length of the sun cycle. */
    public static final int SUN_CYCLE_LENGTH = 30;

    /** The duration of the night cycle. */
    public static final int NIGHT_CYCLE_SUN = 30;

    /** The target framerate of the game. */
    public static final int TARGET_FRAMERATE = 42;

    /** The minimum x-coordinate for generating terrain. */
    public static final int TERRAIN_MIN_X = 0;

    /** The flora instance for managing trees and vegetation. */
    private Flora flora;

    /**
     * The main method to start the game.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Initializes the game with the specified resources and controllers.
     * @param imageReader The image reader for loading images.
     * @param soundReader The sound reader for loading sounds.
     * @param inputListener The user input listener for capturing input events.
     * @param windowController The window controller for managing the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        createBackground(windowController);
        Terrain terrain = createTerrain(windowController);
        createUpperObjects(windowController, terrain);
        createInsideGame(imageReader, inputListener, terrain);
    }

    /**
     * Creates the background elements such as sky, sun, and night.
     * @param windowController The window controller for managing the game window.
     */
    private void createBackground(WindowController windowController) {
        windowController.setTargetFramerate(TARGET_FRAMERATE);
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        GameObject night = Night.create(windowController.getWindowDimensions(), NIGHT_CYCLE_SUN);
        GameObject sun = Sun.create(windowController.getWindowDimensions(), SUN_CYCLE_LENGTH);
        GameObject haloSun = SunHalo.create(sun);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
        gameObjects().addGameObject(haloSun, HALO_SUN_LAYER);
        gameObjects().addGameObject(sun, SUN_LAYER);
        gameObjects().addGameObject(night, Layer.FOREGROUND);
    }

    /**
     * Creates the terrain and adds it to the game objects.
     * @param windowController The window controller for managing the game window.
     * @return The generated terrain.
     */
    private Terrain createTerrain(WindowController windowController) {
        Terrain terrain = new Terrain(windowController.getWindowDimensions(), 1);
        List<Block> blockLists = terrain.createInRange(TERRAIN_MIN_X,
                (int) windowController.getWindowDimensions().x());
        for (Block block : blockLists) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        return terrain;
    }

    /**
     * Creates upper objects such as trees and vegetation.
     * @param windowController The window controller for managing the game window.
     * @param terrain The generated terrain.
     */
    private void createUpperObjects(WindowController windowController, Terrain terrain) {
        flora = new Flora(terrain.callGround);
        for (Tree tree : flora.createInRange(MIN_X_TREES,
                (int) windowController.getWindowDimensions().x())) {
            List<List<GameObject>> list = flora.createLeafs(tree.getxCordinate(), tree.getyCordinate());
            for (GameObject leaf : list.get(0)) {
                gameObjects().addGameObject(leaf, LAYER_LEAF);
            }
            for (GameObject fruit : list.get(1)) {
                gameObjects().addGameObject(fruit, Layer.STATIC_OBJECTS);
            }
            gameObjects().addGameObject(tree, Layer.STATIC_OBJECTS);
        }
    }

    /**
     * Creates objects inside the game such as energy display and avatar.
     * @param imageReader The image reader for loading images.
     * @param inputListener The user input listener for capturing input events.
     * @param terrain The generated terrain.
     */
    private void createInsideGame(ImageReader imageReader, UserInputListener inputListener,
                                  Terrain terrain) {
        Energy energy = new Energy(Vector2.ZERO, new Vector2(ENERGY_DIM, ENERGY_DIM),
                new TextRenderable(String.valueOf(INIT_ENERGY)));
        gameObjects().addGameObject(energy);
        Vector2 pos = new Vector2(INIT_POS_AVATAR, (terrain.groundHeightAt(INIT_POS_AVATAR)
                - Avatar.SIZE_OF_AVATAR));
        GameObject avatar = new Avatar(pos, inputListener, imageReader, energy::updateEnergy,
                flora::isJumping);
        gameObjects().addGameObject(avatar);
    }

    /**
     * Updates the game logic.
     * @param deltaTime The time passed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}
