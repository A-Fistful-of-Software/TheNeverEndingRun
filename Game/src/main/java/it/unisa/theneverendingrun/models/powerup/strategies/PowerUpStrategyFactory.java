package it.unisa.theneverendingrun.models.powerup.strategies;

import it.unisa.theneverendingrun.models.hero.AbstractHero;
import it.unisa.theneverendingrun.models.powerup.PowerUpType;
import it.unisa.theneverendingrun.models.powerup.strategies.impls.ShieldPowerUpStrategy;
import it.unisa.theneverendingrun.models.powerup.strategies.impls.SwordPowerUpStrategy;

public class PowerUpStrategyFactory {

    private AbstractHero hero;

    public PowerUpStrategyFactory(AbstractHero hero) {
        this.hero = hero;
    }

    public PowerUpStrategy createPowerUpStrategy(PowerUpType type) {
        switch (type) {
            case SWORD:
                return new SwordPowerUpStrategy(hero);
            case SHIELD:
                return new ShieldPowerUpStrategy(hero);
            default:
                throw new IllegalArgumentException("Power-up type not valid");
        }
    }
}