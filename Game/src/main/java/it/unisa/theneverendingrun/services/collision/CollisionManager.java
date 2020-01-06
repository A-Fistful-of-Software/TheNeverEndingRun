package it.unisa.theneverendingrun.services.collision;

import it.unisa.theneverendingrun.models.Sprite;
import it.unisa.theneverendingrun.models.enemy.Enemy;
import it.unisa.theneverendingrun.models.hero.AbstractHero;
import it.unisa.theneverendingrun.models.obstacle.Obstacle;
import it.unisa.theneverendingrun.models.powerup.AbstractPowerUp;
import it.unisa.theneverendingrun.models.powerup.PowerUpType;
import it.unisa.theneverendingrun.models.powerup.strategies.PowerUpStrategyFactory;
import it.unisa.theneverendingrun.services.collision.strategies.ObstacleCollisionSideStrategyFactory;
import it.unisa.theneverendingrun.utilities.CollisionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CollisionManager {

    public boolean wasOnObstacle;

    private List<Sprite> collidingSprites;
    private PowerUpStrategyFactory powerUpStrategyFactory;
    private ObstacleCollisionSideStrategyFactory collisionSideStrategyFactory;

    public CollisionManager(AbstractHero hero) {
        collidingSprites = new LinkedList<>();
        powerUpStrategyFactory = new PowerUpStrategyFactory(hero);
        collisionSideStrategyFactory = new ObstacleCollisionSideStrategyFactory(hero);

        wasOnObstacle = false;
    }

    public void checkCollision(AbstractHero hero, List<Sprite> sprites) {

        collidingSprites.stream()
                .filter(sprite -> sprite != null && !CollisionUtils.areColliding(hero, sprite))
                .forEach(sprite -> endCollision(hero, sprite));

        collidingSprites = sprites.stream()
                .filter(sprite -> CollisionUtils.areColliding(hero, sprite))
                .peek(sprite -> beginCollision(hero, sprite))
                .collect(Collectors.toList());
    }

    private void beginCollision(AbstractHero hero, Sprite sprite) {

        var type = sprite.getSpriteImplType();

        switch (type) {

            case OBSTACLE:
                var obstacle = (Obstacle) sprite;

                var side = CollisionUtils.collisionSide(hero.getCollisionBox(), obstacle.getCollisionBox());

                if (side == CollisionSideType.BOTTOM)
                    wasOnObstacle = true;

                var sideStrategy = collisionSideStrategyFactory.createCollisionSideStrategy(side);
                sideStrategy.collide(obstacle);

                break;

            case ENEMY:
                var enemy = (Enemy) sprite;
                enemy.getState().onAttack();
                var swordStrategy = powerUpStrategyFactory.createPowerUpStrategy(PowerUpType.SWORD);
                var killed = swordStrategy.consume();

                if (killed) {
                    enemy.getState().onDie();
                } else {
                    hero.die();
                }

                break;

            case POWER_UP:
                var powerUp = ((AbstractPowerUp) sprite);
                var powerUpType = powerUp.getPowerUpType();
                var strategy = powerUpStrategyFactory.createPowerUpStrategy(powerUpType);
                var collected = strategy.collect();
                if (collected) powerUp.setVisible(false);

                break;

            default:

        }
    }

    private void endCollision(AbstractHero hero, Sprite sprite) {

        var type = sprite.getSpriteImplType();

        switch (type) {

            case OBSTACLE:
                if (wasOnObstacle)
                    hero.getMoveState().onFall();
                wasOnObstacle = false;

                break;

            case ENEMY:
                var enemy = (Enemy) sprite;

                enemy.getState().onIdle();

                break;

            default:

        }
    }

}
