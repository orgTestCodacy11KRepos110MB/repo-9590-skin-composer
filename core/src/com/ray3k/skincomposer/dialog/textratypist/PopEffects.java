package com.ray3k.skincomposer.dialog.textratypist;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.github.tommyettinger.textra.TypingLabel;
import com.ray3k.skincomposer.dialog.textratypist.PopColorPicker.PopColorPickerListener;
import com.ray3k.skincomposer.utils.Utils;
import com.ray3k.stripe.PopTable;

import static com.ray3k.skincomposer.Main.*;
import static com.ray3k.skincomposer.utils.Utils.*;
import static com.ray3k.skincomposer.utils.Utils.onChange;

public class PopEffects extends PopTable {
    private String tagBegin, tagEnd;
    private Table tokenTable;
    private SelectBox<String> effectSelectBox;
    private TypingLabel typingLabel;
    private final static String TEST_STRING = "The quick brown fox jumped over the lazy dog.";
    
    public PopEffects() {
        var style = new PopTableStyle();
        style.background = skin.getDrawable("tt-bg");
        style.stageBackground = skin.getDrawable("tt-stage-background");
    
        setStyle(style);
        setModal(true);
        setKeepSizedWithinStage(true);
        setKeepCenteredInWindow(true);
        setAutomaticallyResized(false);
        pad(10);
        
        defaults().space(5);
        var table = new Table();
        add(table);
        
        table.defaults().space(5);
        var label = new Label("Effect: ", skin, "tt");
        table.add(label);
        
        effectSelectBox = new SelectBox<>(skin, "tt");
        var items = new Array<>(new String[] {"Reset", "Ease", "Hang", "Jump", "Shake", "Sick", "Slide", "Wave", "Wind",
                "Blink", "Fade", "Gradient", "Rainbow", "Jolt", "Wait", "Speed", "Slower", "Slow", "Normal", "Fast",
                "Faster", "Var", "Event"});
        effectSelectBox.setItems(items);
        effectSelectBox.getList().addListener(handListener);
        table.add(effectSelectBox);
        effectSelectBox.addListener(handListener);
        onChange(effectSelectBox, () -> {
            updateTokenTable();
        });
        
        row();
        table = new Table();
        table.setBackground(skin.getDrawable("black"));
        table.left().pad(5);
        add(table).growX().height(50);
        
        typingLabel = new TypingLabel(TEST_STRING, skin, "tt-effect");
        table.add(typingLabel);
        
        row();
        tokenTable = new Table();
        add(tokenTable).grow();
        updateTokenTable();
        
        row();
        table = new Table();
        table.right();
        add(table);
        
        table.defaults().space(5).uniformX().fillX();
        var textButton = new TextButton("OK", skin, "tt");
        table.add(textButton);
        textButton.addListener(handListener);
        onChange(textButton, () -> {
            hide();
            fire(new PopEffectsEvent(tagBegin, tagEnd));
        });
        
        textButton = new TextButton("Cancel", skin, "tt");
        table.add(textButton);
        textButton.addListener(handListener);
        onChange(textButton, () -> {
            hide();
            fire(new PopEffectsEvent());
        });
    }
    
    private void updateTokenTable() {
        tokenTable.clearChildren();
        
        tokenTable.defaults().space(5);
        switch (effectSelectBox.getSelected()) {
            case "Reset":
                break;
            case "Ease":
                tagBegin = "{EASE}";
                tagEnd = "{ENDEASE}";
                typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                typingLabel.restart();
                
                var distanceField = createNumberField(1.0f, "distance", "intensity", "intensity", tokenTable);
                
                tokenTable.row();
                var intensityField = createNumberField(1.0f, "intensity", "distance", "distance", tokenTable);
                
                tokenTable.row();
                var elasticButton = createBooleanField(true, "intensity", tokenTable);
    
                Runnable runnable = () -> {
                    float distance = isNumeric(distanceField.getText())? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    tagBegin = "{EASE=" + distance + ";" + intensity + ";" + elasticButton.isChecked() + "}";
                    
                    typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                    typingLabel.restart();
                };

                onChange(distanceField, runnable);
                onChange(intensityField, runnable);
                onChange(elasticButton, runnable);
                break;
            case "Hang":
                tagBegin = "{HANG}";
                tagEnd = "{ENDHANG}";
                typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                typingLabel.restart();
    
                distanceField = createNumberField(1.0f, "distance", "intensity", "intensity", tokenTable);
    
                tokenTable.row();
                intensityField = createNumberField(1.0f, "intensity", "distance", "distance", tokenTable);
    
                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    tagBegin = "{HANG=" + distance + ";" + intensity + "}";
        
                    typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                    typingLabel.restart();
                };
    
                onChange(distanceField, runnable);
                onChange(intensityField, runnable);
                break;
            case "Jump":
                tagBegin = "{JUMP}";
                tagEnd = "{ENDJUMP}";
                typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                typingLabel.restart();
    
                distanceField = createNumberField(1.0f, "distance", "duration", "frequency", tokenTable);
                
                tokenTable.row();
                var frequencyField = createNumberField(1.0f, "frequency", "distance", "intensity", tokenTable);
    
                tokenTable.row();
                intensityField = createNumberField(1.0f, "intensity", "frequency", "duration", tokenTable);
    
                tokenTable.row();
                var durationField = createNumberField(-1.0f, "duration", "intensity", "distance", tokenTable);
    
                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float frequency = isNumeric(frequencyField.getText()) ? Float.parseFloat(frequencyField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    tagBegin = "{JUMP=" + distance + ";" + frequency + ";" + intensity + (!MathUtils.isEqual(duration, -1)? ";" + duration: "") + "}";
        
                    typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                    typingLabel.restart();
                };
    
                onChange(distanceField, runnable);
                onChange(frequencyField, runnable);
                onChange(intensityField, runnable);
                onChange(durationField, runnable);
                break;
            case "Shake":
                tagBegin = "{SHAKE}";
                tagEnd = "{ENDSHAKE}";
                typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                typingLabel.restart();
    
                distanceField = createNumberField(1.0f, "distance", "duration", "frequency", tokenTable);
    
                tokenTable.row();
                intensityField = createNumberField(1.0f, "intensity", "frequency", "duration", tokenTable);
    
                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "intensity", "distance", tokenTable);
    
                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    tagBegin = "{SHAKE=" + distance + ";" + intensity + (!MathUtils.isEqual(duration, -1)? ";" + duration: "") + "}";
        
                    typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                    typingLabel.restart();
                };
    
                onChange(distanceField, runnable);
                onChange(intensityField, runnable);
                onChange(durationField, runnable);
                break;
            case "Sick":
                tagBegin = "{SICK}";
                tagEnd = "{ENDSICK}";
                typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                typingLabel.restart();
    
                distanceField = createNumberField(1.0f, "distance", "duration", "frequency", tokenTable);
    
                tokenTable.row();
                intensityField = createNumberField(1.0f, "intensity", "frequency", "duration", tokenTable);
    
                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "intensity", "distance", tokenTable);
    
                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    tagBegin = "{SICK=" + distance + ";" + intensity + (!MathUtils.isEqual(duration, -1)? ";" + duration: "") + "}";
        
                    typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                    typingLabel.restart();
                };
    
                onChange(distanceField, runnable);
                onChange(intensityField, runnable);
                onChange(durationField, runnable);
                break;
            case "Slide":
                tagBegin = "{SLIDE}";
                tagEnd = "{ENDSLIDE}";
                typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                typingLabel.restart();
    
                distanceField = createNumberField(1.0f, "distance", "intensity", "intensity", tokenTable);
    
                tokenTable.row();
                intensityField = createNumberField(1.0f, "intensity", "distance", "distance", tokenTable);
    
                tokenTable.row();
                elasticButton = createBooleanField(true, "intensity", tokenTable);
    
                runnable = () -> {
                    float distance = isNumeric(distanceField.getText())? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    tagBegin = "{SLIDE=" + distance + ";" + intensity + ";" + elasticButton.isChecked() + "}";
        
                    typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                    typingLabel.restart();
                };
    
                onChange(distanceField, runnable);
                onChange(intensityField, runnable);
                onChange(elasticButton, runnable);
                break;
            case "Wave":
                tagBegin = "{WAVE}";
                tagEnd = "{ENDWAVE}";
                typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                typingLabel.restart();
    
                distanceField = createNumberField(1.0f, "distance", "duration", "frequency", tokenTable);
    
                tokenTable.row();
                frequencyField = createNumberField(1.0f, "frequency", "distance", "intensity", tokenTable);
    
                tokenTable.row();
                intensityField = createNumberField(1.0f, "intensity", "frequency", "duration", tokenTable);
    
                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "intensity", "distance", tokenTable);
    
                runnable = () -> {
                    float distance = isNumeric(distanceField.getText()) ? Float.parseFloat(distanceField.getText()) : 1.0f;
                    float frequency = isNumeric(frequencyField.getText()) ? Float.parseFloat(frequencyField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    tagBegin = "{WAVE=" + distance + ";" + frequency + ";" + intensity + (!MathUtils.isEqual(duration, -1)? ";" + duration: "") + "}";
        
                    typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                    typingLabel.restart();
                };
    
                onChange(distanceField, runnable);
                onChange(frequencyField, runnable);
                onChange(intensityField, runnable);
                onChange(durationField, runnable);
                break;
            case "Wind":
                tagBegin = "{WIND}";
                tagEnd = "{ENDWIND}";
                typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                typingLabel.restart();
    
                var distanceXfield = createNumberField(1.0f, "distanceX", "duration", "distanceY", tokenTable);
    
                tokenTable.row();
                var distanceYfield = createNumberField(1.0f, "distanceY", "distanceX", "spacing", tokenTable);
    
                tokenTable.row();
                var spacingField = createNumberField(1.0f, "spacing", "distanceY", "intensity", tokenTable);
    
                tokenTable.row();
                intensityField = createNumberField(1.0f, "intensity", "spacing", "duration", tokenTable);
    
                tokenTable.row();
                durationField = createNumberField(-1.0f, "duration", "intensity", "distance", tokenTable);
    
                runnable = () -> {
                    float distanceX = isNumeric(distanceXfield.getText()) ? Float.parseFloat(distanceXfield.getText()) : 1.0f;
                    float distanceY = isNumeric(distanceYfield.getText()) ? Float.parseFloat(distanceYfield.getText()) : 1.0f;
                    float spacing = isNumeric(spacingField.getText()) ? Float.parseFloat(spacingField.getText()) : 1.0f;
                    float intensity = isNumeric(intensityField.getText()) ? Float.parseFloat(intensityField.getText()) : 1.0f;
                    float duration = isNumeric(durationField.getText()) ? Float.parseFloat(durationField.getText()) : -1.0f;
                    tagBegin = "{WIND=" + distanceX + ";" + distanceY + ";" + spacing + ";" + intensity + (!MathUtils.isEqual(duration, -1)? ";" + duration: "") + "}";
        
                    typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                    typingLabel.restart();
                };
    
                onChange(distanceXfield, runnable);
                onChange(distanceYfield, runnable);
                onChange(spacingField, runnable);
                onChange(intensityField, runnable);
                onChange(durationField, runnable);
                break;
            case "Blink":
                tagBegin = "{BLINK}";
                tagEnd = "{ENDBLINK}";
                typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                typingLabel.restart();
    
                var color1 = new Color(Color.WHITE);
                var color1pop = createColorField(color1, "color1", tokenTable);
    
                tokenTable.row();
                var color2 = new Color(Color.WHITE);
                var color2pop = createColorField(color2, "color2", tokenTable);
    
                tokenTable.row();
                frequencyField = createNumberField(1.0f, "frequency", "threshold", "threshold", tokenTable);
    
                tokenTable.row();
                var thresholdField = createNumberField(.5f, "threshold", "frequency", "frequency", tokenTable);
    
                runnable = () -> {
                    float frequency = isNumeric(frequencyField.getText()) ? Float.parseFloat(frequencyField.getText()) : 1.0f;
                    float threshold = isNumeric(thresholdField.getText()) ? Float.parseFloat(thresholdField.getText()) : .5f;
                    tagBegin = "{BLINK=" + color1 + ";" + color2 + ";" + frequency + ";" + threshold + "}";
        
                    typingLabel.setText(tagBegin + TEST_STRING +tagEnd);
                    typingLabel.restart();
                };
    
                color1pop.addListener(new PopColorPickerListener() {
                    @Override
                    public void picked(Color color) {
                        System.out.println("color = " + color);
                        color1.set(color);
                        runnable.run();
                    }
    
                    @Override
                    public void cancelled() {
        
                    }
                });
                color2pop.addListener(new PopColorPickerListener() {
                    @Override
                    public void picked(Color color) {
                        color2.set(color);
                        runnable.run();
                    }
        
                    @Override
                    public void cancelled() {
            
                    }
                });
                onChange(frequencyField, runnable);
                onChange(thresholdField, runnable);
                break;
            case "Fade":
        
                break;
            case "Gradient":
        
                break;
            case "Rainbow":
        
                break;
            case "Jolt":
        
                break;
            case "Wait":
        
                break;
            case "Speed":
        
                break;
            case "Slower":
        
                break;
            case "Slow":
        
                break;
            case "Normal":
        
                break;
            case "Fast":
        
                break;
            case "Faster":
        
                break;
            case "Var":
        
                break;
            case "Event":
        
                break;
        }
    }
    
    private TextField createNumberField(float defaultValue, String name, String previousField, String nextField, Table table) {
        var subTable = new Table();
        
        subTable.defaults().space(5);
        var label = new Label(name, skin, "tt");
        subTable.add(label);
        
        var textField = new TextField(String.format("%.1f", defaultValue), skin, "tt") {
            @Override
            public void next(boolean up) {
                stage.setKeyboardFocus(findActor(up?previousField:nextField));
            }
        };
        textField.setName(name);
        subTable.add(textField).width(50);
        textField.addListener(ibeamListener);
        applyFieldListener(textField);
    
        var buttonTable = new Table();
        subTable.add(buttonTable);
    
        buttonTable.defaults().space(3);
        var imageButton = new ImageButton(skin, "tt-increase");
        buttonTable.add(imageButton);
        imageButton.addListener(handListener);
        onChange(imageButton, () -> {
            if (textField.getText().length() > 0) textField.setText(String.format("%.1f", Float.parseFloat(textField.getText()) + .1f));
            textField.fire(new ChangeEvent());
        });
    
        buttonTable.row();
        imageButton = new ImageButton(skin, "tt-decrease");
        buttonTable.add(imageButton);
        imageButton.addListener(handListener);
        onChange(imageButton, () -> {
            if (textField.getText().length() > 0) textField.setText(String.format("%.1f", Float.parseFloat(textField.getText()) - .1f));
            textField.fire(new ChangeEvent());
        });
        
        table.add(subTable);
        return textField;
    }
    
    private TextButton createBooleanField(boolean defaultValue, String name, Table table) {
        var textButton = new TextButton(name, skin, "tt-toggle");
        textButton.setChecked(defaultValue);
        table.add(textButton);
        textButton.addListener(handListener);
        return textButton;
    }
    
    private PopColorPicker createColorField(Color defaultValue, String name, Table table) {
        var pop = new PopColorPicker(defaultValue);
        
        var subTable = new Table();
        table.add(subTable);
        
        subTable.defaults().space(5);
        var label = new Label(name, skin, "tt");
        subTable.add(label);
        
        var imageButton = new ImageButton(skin, "tt-swatch");
        imageButton.setColor(defaultValue);
        subTable.add(imageButton);
        imageButton.addListener(handListener);
        pop.addListener(new PopColorPickerListener() {
            @Override
            public void picked(Color color) {
                imageButton.setColor(color);
            }
    
            @Override
            public void cancelled() {
        
            }
        });
        onChange(imageButton, () -> pop.show(stage));
        return pop;
    }
    
    private void applyFieldListener(TextField textField) {
        textField.removeListener(textField.getDefaultInputListener());
        textField.addListener(new ClickListener() {
            boolean selectAll;
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!selectAll) ((ClickListener)textField.getDefaultInputListener()).clicked(event, x, y);
                else {
                    textField.selectAll();
                }
            }
            
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                selectAll = stage.getKeyboardFocus() != event.getListenerActor();
                textField.getDefaultInputListener().touchDown(event, x, y, pointer, button);
                return super.touchDown(event, x, y, pointer, button);
            }
            
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                textField.getDefaultInputListener().touchDragged(event, x, y, pointer);
                super.touchDragged(event, x, y, pointer);
            }
            
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                textField.getDefaultInputListener().touchUp(event, x, y, pointer, button);
                super.touchUp(event, x, y, pointer, button);
            }
            
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                return textField.getDefaultInputListener().keyDown(event, keycode);
            }
            
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                return textField.getDefaultInputListener().keyUp(event, keycode);
            }
            
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                return textField.getDefaultInputListener().keyTyped(event, character);
            }
        });
    }
    
    public static class PopEffectsEvent extends Event {
        String tagBegin;
        String tagEnd;
    
        public PopEffectsEvent(String tagBegin, String tagEnd) {
            this.tagBegin = tagBegin;
            this.tagEnd = tagEnd;
        }
    
        public PopEffectsEvent() {
        }
    }
    
    public static abstract class PopEffectsListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (event instanceof PopEffectsEvent) {
                var popEffectsEvent = (PopEffectsEvent) event;
                if (popEffectsEvent.tagBegin != null) accepted(popEffectsEvent.tagBegin, popEffectsEvent.tagEnd);
                else cancelled();
            }
            return false;
        }
        
        public abstract void accepted(String tagBegin, String tagEnd);
        public abstract void cancelled();
    }
    
    public static PopEffects showPopEffects() {
        var pop = new PopEffects();
        pop.show(stage);
        pop.setSize(400, 300);
        return pop;
    }
}