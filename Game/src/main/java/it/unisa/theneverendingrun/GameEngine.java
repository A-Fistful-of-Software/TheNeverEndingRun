package it.unisa.theneverendingrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import it.unisa.theneverendingrun.factory.RunFactory;
import it.unisa.theneverendingrun.models.Sprite;
import it.unisa.theneverendingrun.models.background.AbstractScrollingBackground;
import it.unisa.theneverendingrun.models.hero.Hero;
import it.unisa.theneverendingrun.models.obstacles.AbstractObstacle;
import it.unisa.theneverendingrun.obstaclesManager.ObstaclesManager;
import it.unisa.theneverendingrun.services.ForestFactory;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;

import java.util.LinkedList;


public class GameEngine extends BasicGame {

    static final String GAME_IDENTIFIER = "it.unisa.theneverendingrun";
    private HandlingInput input;
    private SpriteBatch spriteBatch;
    private RunFactory runFactory;
    private Hero hero;
    private AbstractScrollingBackground background;

    private LinkedList<AbstractObstacle> obstacles;

    private static int OFFSET_MEASURE = 72 / 2;

    //random distances
    private float maxJumpingHeight = OFFSET_MEASURE * 3;
    private float standingHeight = OFFSET_MEASURE;
    private float slidingHeight = standingHeight / 2;
    private float standingWidth = (float) OFFSET_MEASURE / 2;
    private float maxSlidingDistance = maxJumpingHeight;
    ObstaclesManager obstaclesManager;

    Sprite sprite;

    @Override
    public void initialise() {
        spriteBatch = new SpriteBatch();
        runFactory = new it.unisa.theneverendingrun.factory.ForestFactory();
        hero = runFactory.createHero();
        obstaclesManager = new ObstaclesManager((float) hero.getJumpMaxElevation(), hero.getHeight(), (float) hero.getMaxSlideRange(), hero.getHeight() / 2, hero.getWidth());
        input = new HandlingInput();
        var factory = new ForestFactory();
        background = factory.createBackground();

        initObstacles();

    }

    private void initObstacles() {
        obstacles = obstaclesManager.getObstacles();
        /*
        obstacles = new Sprite[2];

        obstacles[0] = new Sprite(new Texture("images/pape.png"), 64 * 5, 64);
        obstacles[0].setPosition(hero.getGroundX() * 2, hero.getGroundY() * 3);

        obstacles[1] = new Sprite(new Texture("images/cane.png"), 64 * 3, 64);
        obstacles[1].setPosition(hero.getGroundX() * 4, hero.getGroundY() * 2);*/
    }

    @Override
    public void update(float delta) {
        background.scroll();

        //TODO: Now return NullPointerException because the hero is not real.
        if (!hero.isXAxisVisible(Gdx.graphics.getWidth())) {
            initialise();
        }
        input.getKeyWASD(hero);

        hero.move();

        for (Sprite obstacle : obstacles)
            CollisionManager.checkCollision(hero, obstacle);
    }

    @Override
    public void interpolate(float alpha) {
    }

    @Override
    public void render(Graphics g) {
        AbstractObstacle obs = obstaclesManager.getNewAppropriateObstacle();
        obstaclesManager.update();
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        drawHero();
        drawObstacles();
        spriteBatch.end();
    }

    private void drawObstacles() {
        for (Sprite obstacle : obstacles)
            obstacle.draw(spriteBatch);
    }

    private void drawHero() {
        hero.draw(spriteBatch);
    }
}
