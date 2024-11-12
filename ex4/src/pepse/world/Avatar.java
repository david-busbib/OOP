package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.movement_schemes.PlatformerMovementScheme;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import pepse.world.trees.Flora;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The Avatar class represents the player's character in the game.
 */
 enum Action {
    JUMP,IDLE,RIGHT,LEFT;
}
public class Avatar extends GameObject {

    // Constants for energy depletion rates and other game parameters
    private static final float ENERGY_DOWN_RUN = (float) 0.5;
    private static final float ENERGY_DOWN_JUMP = 10.f;
    private static final int INIT_VELOCITY_SIZE = 600;
    private static final int FRUIT_ENERGY = 10;
    private static final int WAIT_TIME_OPAQ_FRUIT = 30;
    private static final int UNACTIVATE_OPAQ_FLAG = 0;
    private static final int ACTIVATE_OPAQ_FLAG = 1;
    private static final int RUN_X_VEL = 210;
    private static final int JUMP_Y_VEL = 400;
    private static final int IDLE_ENERGY = 1;
    private static final int MAX_ENERGY = 100;
    public static final int SIZE_OF_AVATAR = 40;
    private static final double TIME_BETWEEN_CLIPS = 0.5;
    private static final float GRAVITY = 400;

    // File paths for animation frames
    public static final String[] PHOTO_FILE_NAMES_IDLE = new String[]{
            "assets/idle_0.png",
            "assets/idle_1.png",
            "assets/idle_2.png",
            "assets/idle_3.png"
    };
    private static final String[] PHOTO_PATH_FOR_JUMP = new String[]{
            "assets/jump_0.png",
            "assets/jump_1.png",
            "assets/jump_2.png",
            "assets/jump_3.png"
    };
    public static final String[] PHOTO_PATH_FOR_RUN = {"assets/run_0.png",
            "assets/run_1.png",
            "assets/run_2.png",
            "assets/run_3.png",
            "assets/run_4.png", "assets/run_5.png"
    };
    public static final String FRUIT_UNAVAILABLE = "fruit_unavailable";

    // Animation renderables for different actions
    private final AnimationRenderable animationRenderableIdle;
    private final AnimationRenderable animationRenderableJump;
    private final AnimationRenderable animationRenderableRun;

    private final PlatformerMovementScheme movementScheme;
    private final Consumer<Boolean> isJumping;
    private final UserInputListener inputListener;
    private Consumer<Float> updateEnergy;

    // Avatar's energy level and time
    private float energy = MAX_ENERGY;
    private Action curAction =Action.IDLE;
    private Action last_action = null;

    /**
     * Constructs a new Avatar instance.
     *
     * @param pos           Initial position of the avatar.
     * @param inputListener User input listener for controlling the avatar.
     * @param imageReader   Image reader for loading avatar's images.
     * @param updateEnergy  Consumer function to update energy.
     * @param isJumping     Consumer function to indicate if the avatar is jumping.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader,
                  Consumer<Float> updateEnergy, Consumer<Boolean> isJumping) {
        super(pos, new Vector2(SIZE_OF_AVATAR, SIZE_OF_AVATAR),
                imageReader.readImage(PHOTO_FILE_NAMES_IDLE[0], true));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.movementScheme = new PlatformerMovementScheme(this,
                inputListener.keyboardMovementDirector());
        movementScheme.setVelocitySize(INIT_VELOCITY_SIZE);
        this.updateEnergy = updateEnergy;
        this.isJumping = isJumping;
        // Initialize animation renderables
        animationRenderableIdle = new
                AnimationRenderable(PHOTO_FILE_NAMES_IDLE, imageReader, true, TIME_BETWEEN_CLIPS);
        animationRenderableJump = new
                AnimationRenderable(PHOTO_PATH_FOR_JUMP, imageReader, true, TIME_BETWEEN_CLIPS);
        animationRenderableRun = new
                AnimationRenderable(PHOTO_PATH_FOR_RUN, imageReader, true, TIME_BETWEEN_CLIPS);
    }

    /**
     * Updates the avatar's state.
     *
     * @param deltaTime Time elapsed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateMovement();
        updateEnergy.accept(energy);
    }

    /**
     * Handles collision events.
     *
     * @param other     The other GameObject involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(Flora.FRUIT_TAG)) {
            updateEnergy(FRUIT_ENERGY);
            changeTag(other, FRUIT_UNAVAILABLE);
            setOpaq(other, UNACTIVATE_OPAQ_FLAG);
            ScheduledTask scheduledTask = new ScheduledTask(this,
                    WAIT_TIME_OPAQ_FRUIT,
                    false, () -> {
                changeTag(other, Flora.FRUIT_TAG);
                setOpaq(other, ACTIVATE_OPAQ_FLAG);
            });
        }
    }

    /**
     * Changes the tag of a GameObject.
     *
     * @param other   The GameObject whose tag to change.
     * @param newTag  The new tag.
     */
    private static void changeTag(GameObject other, String newTag) {

        other.setTag(newTag);
    }

    /**
     * Sets the opacity of a GameObject.
     *
     * @param other  The GameObject whose opacity to set.
     * @param ok     The opacity value.
     */
    private static void setOpaq(GameObject other, int ok) {
        other.renderer().setOpaqueness(ok);
    }

    /**
     * Updates the avatar's movement based on user input.
     */
    private void updateMovement() {
        float yVel = 0;
        boolean isRun = moveRightAndLeft();
        boolean isJump =(inputListener.isKeyPressed(KeyEvent.VK_SPACE));
        if (! isRun  && getVelocity().y() == 0 ) {
            boolean canAct = false;
            if (isJump){
                canAct = updateEnergyDown(ENERGY_DOWN_JUMP);
                if (canAct) {
                    curAction = Action.JUMP;
                    yVel -= JUMP_Y_VEL;
                    change_photo_idle(false);
                    isJumping.accept(true);
                }
                transform().setVelocityY(yVel);

            }
            if ( !canAct ){
                curAction = Action.IDLE;
                Map<String, Integer> b=new HashMap<>();
                updateEnergy(IDLE_ENERGY);
                change_photo_idle(false);
            }
        }
        else {
            isJumping.accept(false);
        }
    }

    private boolean moveRightAndLeft() {
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            boolean canAct = updateEnergyDown(ENERGY_DOWN_RUN);
            if (canAct) {
                curAction = Action.LEFT;
                xVel -= RUN_X_VEL;
                change_photo_idle( true);
            }
        }
        else if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            boolean canAct = updateEnergyDown(ENERGY_DOWN_RUN);
            if (canAct) {
                curAction = Action.RIGHT;
                xVel += RUN_X_VEL;
                change_photo_idle( false);
            }
        }
        transform().setVelocityX(xVel);
        return xVel!=0;
    }


    /**
     * Changes the avatar's renderable based on its state.
     *
     * @param isLeft Indicates if the avatar is facing left.
     */
    private void change_photo_idle(boolean isLeft) {

        if(last_action != curAction) {
            switch (curAction) {
                case IDLE:
                    renderer().setRenderable(animationRenderableIdle);
                    break;
                case JUMP:
                    renderer().setRenderable(animationRenderableJump);
                    break;
                case LEFT:
                case RIGHT:
                    renderer().setRenderable(animationRenderableRun);
                    renderer().setIsFlippedHorizontally(isLeft);
                    break;

            }
        }
        last_action = curAction;
    }

    /**
     * Updates the avatar's energy level by reducing it.
     *
     * @param factor The energy reduction factor.
     * @return True if energy was reduced, false if energy is insufficient.
     */
    private boolean updateEnergyDown(float factor) {
        if (energy >= factor) {
            energy -= factor;
            return true;
        }
        return false;
    }

    /**
     * Updates the avatar's energy level by increasing it.
     *
     * @param factor The energy increase factor.
     */
    public void updateEnergy(float factor) {
        if (energy < MAX_ENERGY - factor) {
            energy += factor;
        } else {
            energy = MAX_ENERGY;
        }

    }
}
