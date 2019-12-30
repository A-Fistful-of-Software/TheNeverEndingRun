package it.unisa.theneverendingrun.gameStates;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TextButtonDecorator extends TextButton {

    private TextButton wrapper;

    public TextButtonDecorator(TextButton button, String text, Skin skin) {
        super(text, skin);
        wrapper = button;
    }

    public TextButtonDecorator(TextButton button) {
        this(button, button.getText().toString(), button.getSkin());
    }

}
