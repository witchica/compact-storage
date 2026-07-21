package com.witchica.compactstorage.client.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Small flat square icon-style button (18x18, same footprint as an inventory slot) with its own
 * plain background instead of vanilla's beveled Button sprite - the vanilla widget reads as a
 * disconnected UI element bolted onto this mod's custom-drawn GUI; a slot-sized flat square sits
 * naturally alongside the storage grid instead (in the spirit of Inventory Profiles Next's compact
 * icon toolbar). Momentary action if activeState is omitted; a persistent toggle (highlighted
 * background while true) if it's supplied. A texture icon can replace the text label, and a tiny
 * live-updating subscript (eg. a modifier-key indicator) can be drawn in the corner on top of it.
 */
public class IconButton extends AbstractWidget {
    public static final int SIZE = 18;
    private static final int ICON_TEXTURE_SIZE = 32;
    private static final float LABEL_SCALE = 0.7f;
    private static final float SUBSCRIPT_SCALE = 0.55f;

    private final Font font;
    private final Runnable onPress;
    private final BooleanSupplier activeState;
    private final Identifier icon;
    private final Supplier<String> subscript;
    private Integer accentColor;
    private Runnable onRightClick;

    public IconButton(int x, int y, Component label, Font font, Runnable onPress) {
        this(x, y, SIZE, label, font, onPress, null, null, null);
    }

    public IconButton(int x, int y, Component label, Font font, Runnable onPress, BooleanSupplier activeState) {
        this(x, y, SIZE, label, font, onPress, activeState, null, null);
    }

    /** A text-label button at a custom footprint, for corner clusters too tight for the default 18px size. */
    public IconButton(int x, int y, int size, Component label, Font font, Runnable onPress, BooleanSupplier activeState) {
        this(x, y, size, label, font, onPress, activeState, null, null);
    }

    /** An icon-textured button (eg. an arrow) instead of a text label, with an optional live subscript badge. */
    public IconButton(int x, int y, int size, Identifier icon, Component tooltipLabel, Font font, Runnable onPress, Supplier<String> subscript) {
        this(x, y, size, tooltipLabel, font, onPress, null, icon, subscript);
    }

    private IconButton(int x, int y, int size, Component label, Font font, Runnable onPress, BooleanSupplier activeState, Identifier icon, Supplier<String> subscript) {
        super(x, y, size, size, label);
        this.font = font;
        this.onPress = onPress;
        this.activeState = activeState;
        this.icon = icon;
        this.subscript = subscript;
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        if(event.button() == 1 && onRightClick != null) {
            onRightClick.run();
        } else {
            onPress.run();
        }
    }

    /** Accept right-click too, but only once something is actually listening for it. */
    @Override
    protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
        return buttonInfo.button() == 0 || (buttonInfo.button() == 1 && onRightClick != null);
    }

    /** Tints the button's background toward the given RGB (eg. the storage type's own color) instead of flat gray. */
    public IconButton accent(Integer rgb) {
        this.accentColor = rgb;
        return this;
    }

    /** Right-click runs this instead of the normal left-click action - eg. cycling a selector backwards. */
    public IconButton onRightClick(Runnable onRightClick) {
        this.onRightClick = onRightClick;
        return this;
    }

    private static int shade(int rgb, float factor) {
        int r = Math.min(255, (int) (((rgb >> 16) & 0xFF) * factor));
        int g = Math.min(255, (int) (((rgb >> 8) & 0xFF) * factor));
        int b = Math.min(255, (int) ((rgb & 0xFF) * factor));
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int x = getX();
        int y = getY();
        boolean active = activeState != null && activeState.getAsBoolean();

        // Mouse hover only, not isHoveredOrFocused() - these are momentary action buttons, not
        // keyboard-navigable controls, and a click leaves them holding keyboard focus, which made
        // isHoveredOrFocused() keep the hover highlight lit long after the mouse moved away.
        int background = active ? 0xFF3A6B3A : (isHovered()
                ? (accentColor != null ? shade(accentColor, 0.55f) : 0xFF5A5A5A)
                : (accentColor != null ? shade(accentColor, 0.35f) : 0xFF373737));
        graphics.fill(x, y, x + width, y + height, background);
        graphics.fill(x, y, x + width, y + 1, 0xFF8B8B8B);
        graphics.fill(x, y, x + 1, y + height, 0xFF8B8B8B);
        graphics.fill(x, y + height - 1, x + width, y + height, 0xFF1E1E1E);
        graphics.fill(x + width - 1, y, x + width, y + height, 0xFF1E1E1E);

        if(icon != null) {
            int iconSize = Math.min(width, height) - 4;
            int iconX = x + (width - iconSize) / 2;
            int iconY = y + (height - iconSize) / 2;
            graphics.blit(RenderPipelines.GUI_TEXTURED, icon, iconX, iconY, 0, 0, iconSize, iconSize,
                    ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE, ICON_TEXTURE_SIZE, 0xFFE0E0E0);
        } else {
            Component label = getMessage();
            float scale = LABEL_SCALE;
            graphics.pose().pushMatrix();
            graphics.pose().translate(x + (width - font.width(label) * scale) / 2f, y + (height - font.lineHeight * scale) / 2f);
            graphics.pose().scale(scale, scale);
            graphics.text(font, label, 0, 0, 0xFFE0E0E0, false);
            graphics.pose().popMatrix();
        }

        if(subscript != null) {
            String text = subscript.get();
            float scale = SUBSCRIPT_SCALE;
            graphics.pose().pushMatrix();
            graphics.pose().translate(x + width - (font.width(text) * scale) - 1, y + height - (font.lineHeight * scale) - 1);
            graphics.pose().scale(scale, scale);
            graphics.text(font, text, 0, 0, 0xFFFFD966, false);
            graphics.pose().popMatrix();
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        defaultButtonNarrationText(narrationElementOutput);
    }
}
