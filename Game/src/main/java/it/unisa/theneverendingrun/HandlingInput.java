package it.unisa.theneverendingrun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import it.unisa.theneverendingrun.models.hero.Hero;

public class HandlingInput {

    public void getKeyWASD(Hero hero) {

        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            hero.getFacingState().onRight();
            hero.setDx(5);
        } else
            hero.setDx(0);

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            hero.getMoveState().onJump();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            hero.getFacingState().onLeft();
            hero.setDx(5);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            hero.getMoveState().onSlide();
        }

    }
}