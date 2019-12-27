package it.unisa.theneverendingrun.obstaclesManager;

import com.badlogic.gdx.Gdx;
import it.unisa.theneverendingrun.CollisionManager;
import it.unisa.theneverendingrun.models.Spawnable;
import it.unisa.theneverendingrun.models.SpawnableTypes;
import it.unisa.theneverendingrun.models.hero.Hero;
import it.unisa.theneverendingrun.services.ForestFactory;

import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnableManager {

    /**
     * Values which are needed to set the correct position of the new obstacle.
     */
    private int spawnProbability = 50;
    private int minimumDistanceMultiplier = 3;
    private float offset;

    /**
     * Reference to the forestFactory.
     */
    private ForestFactory forestFactory;

    /**
     * Reference to the last obstacle generated.
     */
    private Spawnable lastObstacle;

    /**
     * The type of the last obstacle added to the game
     */
    private SpawnableTypes lastObstacleType;

    /**
     * Reference to the hero which the measures will be based on.
     */
    private Hero hero;

    /**
     * Constructor of the obstaclesManager. parameters are self explanatory
     * todo update and complete this javadoc
     */
    public SpawnableManager() {
        forestFactory = new ForestFactory();
        hero = forestFactory.createHero();
        offset = forestFactory.createHero().getGroundY();
    }

    /**
     * This method will randomly create and return a new obstacle. The obstacles are generated by following some
     * criteria, ensuring that the character can avoid it.
     * In addition, to the obstacle will be assigned the correct position, based on the reference measures given
     * during the creation of the obstaclesManager.
     *
     * @return A new Spawnable, with the correct position, null if the obstacle cannot be generated
     */
    public Spawnable generateNewObstacle() {
        SpawnableTypes newObstacleType = getAppropriateSpawnableType();
        if (newObstacleType == null) {
            return null;
        }
        Spawnable newObstacle = getObstacle(newObstacleType);
        setPosition(newObstacle, newObstacleType);
        lastObstacle = newObstacle;
        lastObstacleType = newObstacleType;
        return newObstacle;
    }

    private Spawnable getObstacle(SpawnableTypes newObstacleType) {
        switch (newObstacleType) {
            case Wolf:
                return forestFactory.createWolf();
            case Golem:
                return forestFactory.createGolem();
            case Witch:
                return forestFactory.createWitch();
            case Jumpable:
                return forestFactory.createJumpableObstacle();
            case JumpableSlidable:
                return forestFactory.createJumpableSlidableObstacle();
            case Slidable:
                return forestFactory.createSlidableObstacle();
            default:
                return null;
        }
    }

    /**
     * This method is used to get the right type of obstacle that can be added to the path, following the conditions.
     * For example, if the last obstacle was a slidable one, we cannot put another right after it,
     * otherwise the player might not be able to pass.
     * Please, note that this method randomly decides to add or not an obstacle, even if it can added.
     *
     * @return The type of obstacle that can be added, null if none.
     */
    private SpawnableTypes getAppropriateSpawnableType() {
        //If there isn't any obstacle on the screen, add one at random
        if (lastObstacle == null || !lastObstacle.isXAxisVisible()) {
            int random = ThreadLocalRandom.current().nextInt(SpawnableTypes.values().length);
            return SpawnableTypes.values()[random];
        }

        // Calculate the distance from the last obstacle. This distance is defined as the distance from the right
        // side of an obstacle to the left side of the view.
        int distance = (int) (Gdx.graphics.getWidth() - lastObstacle.getX() - lastObstacle.getWidth());

        // If distance is less than zero, the obstacle is still not completely visible, so wait
        if (distance < 0)
            return null;

        // If distance is zero, then we could add a jumpable or slidable obstacle, but only if the previous was jumpable
        if (distance == 0) {
            if (lastObstacleType == SpawnableTypes.Jumpable) {
                //generating a value between -1 and 1, deciding what to add according to it
                int r = ThreadLocalRandom.current().nextInt(-1, 2);
                if (r == 0)
                    return null;
                if (r > 0)
                    return SpawnableTypes.Jumpable;
                if (r < 0)
                    return SpawnableTypes.Slidable;
            }
            if (lastObstacleType == SpawnableTypes.JumpableSlidable) {
                return null;
            }
            if (lastObstacleType == SpawnableTypes.Slidable) {
                return null;
            }
            if (lastObstacleType == SpawnableTypes.Wolf || lastObstacleType == SpawnableTypes.Golem) {
                //fixme, is this always jumpable?
                if (ThreadLocalRandom.current().nextBoolean())
                    return SpawnableTypes.Jumpable;
            }
        }

        //If the space is not sufficient for the hero to pass, wait.
        if (distance < hero.getStandardWidth() * minimumDistanceMultiplier) {
            return null;
        }


        // If the obstacle is distant enough, it is possible to add every type of obstacle
        if (distance >= hero.getStandardWidth() * minimumDistanceMultiplier) {
            if (ThreadLocalRandom.current().nextInt() % spawnProbability == 0) {
                int random = ThreadLocalRandom.current().nextInt(0, SpawnableTypes.values().length);
                return SpawnableTypes.values()[random];
            }
        }
        return null;
    }

    /**
     * This method will fix the position of the given obstacle. This will take into account the dimensions of the
     * obstacles, allowing to vary the position, keeping it avoidable by the user. If the position was already fixed,
     * it will change randomly, but always in a range that allows to avoid it.
     * The position on the x axis is always at the rightmost edge, while on the y-axis depends on the dimension of the
     * obstacle and on the parameters passed to the constructor.
     *
     * @param obstacle the obstacle which needs the position fixed
     * @param newObstacleType
     */
    private void setPosition(Spawnable obstacle, SpawnableTypes newObstacleType) {
        int yPosition = 0;

        if (newObstacleType == SpawnableTypes.Jumpable) {
            yPosition = 0;
        }

        if (newObstacleType == SpawnableTypes.Wolf || newObstacleType == SpawnableTypes.Golem) {
            yPosition = 0;
        }

        if (newObstacleType == SpawnableTypes.Slidable) {
            yPosition = ThreadLocalRandom.current().nextInt(
                    (int) hero.getStandardHeight() / 2,
                    (int) hero.getStandardHeight() - 1
            );
            if (lastObstacleType == SpawnableTypes.Jumpable && lastObstacle.getX() + lastObstacle.getWidth() >= Gdx.graphics.getWidth() - 1) {
                yPosition += lastObstacle.getHeight() + lastObstacle.getY() - offset;
                }
            }


        if (newObstacleType == SpawnableTypes.JumpableSlidable) {
            yPosition = ThreadLocalRandom.current().nextInt(
                    (int) hero.getStandardHeight() / 2,
                    (int) hero.getStandardHeight() / 2 + (int) hero.getJumpMaxElevation() - (int) obstacle.getHeight());
        }
        // Accounting for the lower part of the background
        yPosition += offset;
        obstacle.setPosition(Gdx.graphics.getWidth(), yPosition);
    }

    /**
     * This method will remove from memory the obstacles which are not visible anymore.
     *
     * @param obstacles the LinkedList which contains all the obstacles.
     */
    public void clearOldObstacles(LinkedList<Spawnable> obstacles) {
        if (obstacles.isEmpty())
            return;

        LinkedList<Spawnable> toRemoveList = new LinkedList<>();
        for (Spawnable obstacle : obstacles)
            if (lastObstacle.getX() + lastObstacle.getWidth() + hero.getWidth() < 0) {
                toRemoveList.add(obstacle);
            }
        for (Spawnable toRemove : toRemoveList) {
            obstacles.remove(toRemove);
            CollisionManager.wasOnObstacle.remove(toRemove);
        }
    }

    /**
     * Set the spawn probability of an obstacle, when anyone can be generated. This value is inversely proportional to
     * the probability of spawning. This means that if it is equal to 1, every time the minimum distance is
     * exceeded, than an obstacle is generated.
     *
     * @param spawnProbability the spawn probability of an obstacle when any can be generated
     */
    public void setSpawnProbability(int spawnProbability) {
        if (spawnProbability < 1)
            throw new IllegalArgumentException("SpawnProbability must be greater or equal than 1.");

        this.spawnProbability = spawnProbability;
    }

    /**
     * Set the minimum distance that must occur between two obstacles. This number is a multiplier of the hero's width.
     * This means that a multiplier of one, allows the hero to pass between two obstacles leaving zero pixel on its left
     * or on its right. Obviously, the multiplier cannot be less than one, since the distance must be greater than the
     * hero width.
     *
     * @param minimumDistanceMultiplier the multiplier
     * @throws IllegalArgumentException if the argument is less than 1
     */
    public void setMinimumDistanceMultiplier(int minimumDistanceMultiplier) {
        if (minimumDistanceMultiplier < 1)
            throw new IllegalArgumentException("MinimumDistanceMultiplier must be greater or equal than 1.");
        this.minimumDistanceMultiplier = minimumDistanceMultiplier;
    }

    @Deprecated
    public void updateObstaclesPosition(LinkedList<Spawnable> obstacles) {
        for (Spawnable obs : obstacles
        ) {
            obs.setX(obs.getX() - 8);
        }
    }
}
