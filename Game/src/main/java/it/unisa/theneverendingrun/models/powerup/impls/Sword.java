package it.unisa.theneverendingrun.models.powerup.impls;

import com.badlogic.gdx.graphics.Texture;
import it.unisa.theneverendingrun.models.powerup.AbstractPowerUp;
import it.unisa.theneverendingrun.models.powerup.PowerUpType;

/**
 * An implementation of {@link AbstractPowerUp}
 */
public class Sword extends AbstractPowerUp {

    /**
     *
     * @see AbstractPowerUp#AbstractPowerUp(Texture, float)
     *
     * Create an {@link AbstractPowerUp}
     */
    public Sword(Texture texture) {
        this(texture, 1);
    }

    /**
     *
     * @see AbstractPowerUp#AbstractPowerUp(Texture, float)
     *
     * Create an {@link AbstractPowerUp}
     */
    public Sword(Texture texture, float scaleFactor) {
        super(texture, scaleFactor);
    }


    /**
     * @return the the type of the powerup
     *
     * @see PowerUpType
     */
    @Override
    public PowerUpType getPowerUpType() {
        return PowerUpType.SWORD;
    }
}
