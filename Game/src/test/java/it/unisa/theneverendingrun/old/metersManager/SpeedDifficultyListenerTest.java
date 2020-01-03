package it.unisa.theneverendingrun.old.metersManager;

import de.tomgrill.gdxtesting.GdxTestRunner;
import it.unisa.theneverendingrun.MetersManagerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

@RunWith(GdxTestRunner.class)
public class SpeedDifficultyListenerTest {
    private MetersManagerFactory factory = new MetersManagerFactory();

    @Test
    public void test() {
        var steps = new Random().nextInt(10000);

        for (int i = 0; i < steps; i++) {
            factory.computeMeters();
            /*Assert.assertEquals(factory.getInitialSpeed() +
                    factory.getSpeedFactor() *
                            (factory.getDifficulty() - factory.getInitialDifficulty()), factory.getSpeed(), 0.0);*/
        }
    }
}