package dev.tr7zw.trender.gui.widget;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import dev.tr7zw.trender.gui.client.BackgroundPainter;
import dev.tr7zw.trender.gui.client.RenderContext;
import dev.tr7zw.trender.gui.client.ScreenDrawing;
import dev.tr7zw.trender.gui.impl.client.NarrationMessages;
import dev.tr7zw.trender.gui.widget.data.InputResult;
import dev.tr7zw.transition.mc.ComponentProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
//#if MC >= 11800
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
//#endif
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class WTextField extends WWidget {
    public static final int TEXT_PADDING_X = 4;
    public static final int TEXT_PADDING_Y = 6;
    public static final int CURSOR_PADDING_Y = 4;
    public static final int CURSOR_HEIGHT = 12;

    @Nullable
    private Font textRenderer;

    private String text = "";
    private int maxLength = 16;
    private boolean editable = true;
    private int tickCount = 0;

    private int disabledColor = 0xFF707070;
    private int enabledColor = 0xFFE0E0E0;
    private int suggestionColor = 0xFF808080;

    private static final int BACKGROUND_COLOR = 0xFF000000;
    private static final int BORDER_COLOR_SELECTED = 0xFFFFFFA0;
    private static final int BORDER_COLOR_UNSELECTED = 0xFFA0A0A0;
    private static final int CURSOR_COLOR = 0xFFD0D0D0;

    @Nullable
    private Component suggestion = null;

    // Index of the leftmost character to be rendered.
    private int scrollOffset = 0;

    private int cursor = 0;
    /**
     * If not -1, select is the "anchor point" of a selection. That is, if you hit
     * shift+left with no existing selection, the selection will be anchored to
     * where you were, but the cursor will move left, expanding the selection as you
     * continue to move left. If you move to the right, eventually you'll overtake
     * the anchor, drop the anchor at the same place and start expanding the
     * selection rightwards instead.
     */
    private int select = -1;

    private Consumer<String> onChanged;

    private Predicate<String> textPredicate;

    @Nullable
    private BackgroundPainter backgroundPainter;

    public WTextField() {
    }

    public WTextField(Component suggestion) {
        this.suggestion = suggestion;
    }

    /**
     * Sets the text of this text field. If the text is more than the
     * {@linkplain #getMaxLength() max length}, it'll be shortened to the max
     * length.
     *
     * @param s the new text
     */
    public void setText(String s) {
        setTextWithResult(s);
    }

    private boolean setTextWithResult(String s) {
        if (this.textPredicate == null || this.textPredicate.test(s)) {
            this.text = (s.length() > maxLength) ? s.substring(0, maxLength) : s;
            // Call change listener
            if (onChanged != null)
                onChanged.accept(this.text);
            // Reset cursor if needed
            cursor = clampCursor(cursor);
            return true;
        }

        return false;
    }

    /**
     * {@return the text in this text field}
     */
    public String getText() {
        return this.text;
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        this.tickCount++;
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(x, 20);
    }

    private int clampCursor(int cursor) {
        return Mth.clamp(cursor, 0, text.length());
    }

    public void setCursorPos(int location) {
        this.cursor = clampCursor(location);
        scrollCursorIntoView();
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public int getCursor() {
        return this.cursor;
    }

    private Font getTextRenderer() {
        return textRenderer != null ? textRenderer : (textRenderer = Minecraft.getInstance().font);
    }

    public void scrollCursorIntoView() {
        Font font = getTextRenderer();

        if (scrollOffset > cursor) {
            scrollOffset = cursor;
        }
        if (scrollOffset < cursor
                && font.plainSubstrByWidth(text.substring(scrollOffset), getWidth() - TEXT_PADDING_X * 2).length()
                        + scrollOffset < cursor) {
            scrollOffset = cursor;
        }

        checkScrollOffset();
    }

    private void checkScrollOffset() {
        int rightMostScrollOffset = text.length()
                - getTextRenderer().plainSubstrByWidth(text, getWidth() - TEXT_PADDING_X * 2, true).length();
        scrollOffset = Math.min(rightMostScrollOffset, scrollOffset);
    }

    @Nullable
    public String getSelection() {
        if (select < 0)
            return null;
        if (select == cursor)
            return null;

        //Tidy some things
        if (select > text.length())
            select = text.length();
        cursor = clampCursor(cursor);

        int start = Math.min(select, cursor);
        int end = Math.max(select, cursor);

        return text.substring(start, end);
    }

    public boolean isEditable() {
        return this.editable;
    }

    protected void renderBox(RenderContext context, int x, int y) {
        int borderColor = this.isFocused() ? BORDER_COLOR_SELECTED : BORDER_COLOR_UNSELECTED;
        ScreenDrawing.coloredRect(context, x - 1, y - 1, getWidth() + 2, getHeight() + 2, borderColor);
        ScreenDrawing.coloredRect(context, x, y, getWidth(), getHeight(), BACKGROUND_COLOR);
    }

    protected void renderText(RenderContext context, int x, int y, String visibleText) {
        int textColor = this.editable ? this.enabledColor : this.disabledColor;
        context.drawString(getTextRenderer(), visibleText, x + TEXT_PADDING_X, y + TEXT_PADDING_Y, textColor, true);
    }

    protected void renderCursor(RenderContext context, int x, int y, String visibleText) {
        if (this.tickCount / 6 % 2 == 0)
            return;
        if (this.cursor < this.scrollOffset)
            return;
        if (this.cursor > this.scrollOffset + visibleText.length())
            return;
        int cursorOffset = getTextRenderer().width(visibleText.substring(0, this.cursor - this.scrollOffset));
        ScreenDrawing.coloredRect(context, x + TEXT_PADDING_X + cursorOffset, y + CURSOR_PADDING_Y, 1, CURSOR_HEIGHT,
                CURSOR_COLOR);
    }

    protected void renderSuggestion(RenderContext context, int x, int y) {
        if (this.suggestion == null)
            return;
        context.drawString(getTextRenderer(), suggestion, x + TEXT_PADDING_X, y + TEXT_PADDING_Y, this.suggestionColor,
                true);
    }

    protected void renderSelection(RenderContext context, int x, int y, String visibleText) {
        if (select == cursor || select == -1)
            return;

        int textLength = visibleText.length();

        int left = Math.min(cursor, select);
        int right = Math.max(cursor, select);

        if (right < scrollOffset || left > scrollOffset + textLength)
            return;

        int normalizedLeft = Math.max(scrollOffset, left) - scrollOffset;
        int normalizedRight = Math.min(scrollOffset + textLength, right) - scrollOffset;

        Font font = getTextRenderer();
        int leftCaret = font.width(visibleText.substring(0, normalizedLeft));
        int selectionWidth = font.width(visibleText.substring(normalizedLeft, normalizedRight));

        context.invertedRect(x + TEXT_PADDING_X + leftCaret, y + CURSOR_PADDING_Y, selectionWidth, CURSOR_HEIGHT);
    }

    protected void renderTextField(RenderContext context, int x, int y) {
        checkScrollOffset();
        String visibleText = getTextRenderer().plainSubstrByWidth(this.text.substring(this.scrollOffset),
                this.getWidth() - 2 * TEXT_PADDING_X);
        renderBox(context, x, y);
        renderText(context, x, y, visibleText);
        if (this.text.isEmpty() && !this.isFocused()) {
            renderSuggestion(context, x, y);
        }
        if (this.isFocused()) {
            renderCursor(context, x, y, visibleText);
        }
        renderSelection(context, x, y, visibleText);
    }

    public WTextField setTextPredicate(Predicate<String> predicate_1) {
        this.textPredicate = predicate_1;
        return this;
    }

    public WTextField setChangedListener(Consumer<String> listener) {
        this.onChanged = listener;
        return this;
    }

    public WTextField setMaxLength(int max) {
        this.maxLength = max;
        if (this.text.length() > max) {
            setText(this.text.substring(0, max));
        }
        return this;
    }

    public WTextField setEnabledColor(int col) {
        this.enabledColor = col;
        return this;
    }

    public WTextField setSuggestionColor(int suggestionColor) {
        this.suggestionColor = suggestionColor;
        return this;
    }

    public WTextField setDisabledColor(int col) {
        this.disabledColor = col;
        return this;
    }

    public WTextField setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    @Nullable
    public Component getSuggestion() {
        return suggestion;
    }

    public WTextField setSuggestion(@Nullable Component suggestion) {
        this.suggestion = suggestion;
        return this;
    }

    public WTextField setBackgroundPainter(BackgroundPainter painter) {
        this.backgroundPainter = painter;
        return this;
    }

    public boolean canFocus() {
        return true;
    }

    @Override
    public void onFocusGained() {
    }

    @Override
    public void paint(RenderContext context, int x, int y, int mouseX, int mouseY) {
        renderTextField(context, x, y);
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        requestFocus();
        cursor = getCaretPosition(x - TEXT_PADDING_X);
        scrollCursorIntoView();
        return InputResult.PROCESSED;
    }

    public int getCaretPosition(int clickX) {
        if (clickX < 0)
            return 0;
        int lastPos = 0;
        checkScrollOffset();
        String string = text.substring(scrollOffset);
        Font font = getTextRenderer();
        for (int i = 0; i < string.length(); i++) {
            int w = font.width(string.charAt(i) + "");
            if (lastPos + w >= clickX) {
                if (clickX - lastPos < w / 2) {
                    return i + scrollOffset;
                }
            }
            lastPos += w;
        }
        return string.length();
    }

    @Override
    public InputResult onCharTyped(char ch) {
        if (!isEditable())
            return InputResult.IGNORED;
        insertText(ch + "");
        return InputResult.PROCESSED;
    }

    private void insertText(String toInsert) {
        String before, after;
        if (select != -1 && select != cursor) {
            int left = Math.min(cursor, select);
            int right = Math.max(cursor, select);
            before = this.text.substring(0, left);
            after = this.text.substring(right);
        } else {
            before = this.text.substring(0, cursor);
            after = this.text.substring(cursor);
        }
        if (before.length() + after.length() + toInsert.length() > maxLength)
            return;
        if (setTextWithResult(before + toInsert + after)) {
            select = -1;
            cursor = (before + toInsert).length();
            scrollCursorIntoView();
        }
    }

    private void copySelection() {
        String selection = getSelection();
        if (selection != null) {
            Minecraft.getInstance().keyboardHandler.setClipboard(selection);
        }
    }

    private void paste() {
        String clip = Minecraft.getInstance().keyboardHandler.getClipboard();
        insertText(clip);
    }

    private void deleteSelection() {
        int left = Math.min(cursor, select);
        int right = Math.max(cursor, select);
        if (setTextWithResult(text.substring(0, left) + text.substring(right))) {
            select = -1;
            cursor = clampCursor(left);
            scrollCursorIntoView();
        }
    }

    private void delete(int modifiers, boolean backwards) {
        if (select == -1 || select == cursor) {
            select = skipCharacters((GLFW.GLFW_MOD_CONTROL & modifiers) != 0, backwards ? -1 : 1);
        }
        deleteSelection();
    }

    private int skipCharacters(boolean skipMany, int direction) {
        if (direction != -1 && direction != 1)
            return cursor;
        int position = cursor;
        while (true) {
            position += direction;
            if (position < 0) {
                return 0;
            }
            if (position > text.length()) {
                return text.length();
            }
            if (!skipMany)
                return position;
            if (position < text.length() && Character.isWhitespace(text.charAt(position))) {
                return position;
            }
        }
    }

    public void onDirectionalKey(int direction, int modifiers) {
        if ((GLFW.GLFW_MOD_SHIFT & modifiers) != 0) {
            if (select == -1 || select == cursor)
                select = cursor;
            cursor = skipCharacters((GLFW.GLFW_MOD_CONTROL & modifiers) != 0, direction);
        } else {
            if (select != -1) {
                cursor = direction < 0 ? Math.min(cursor, select) : Math.max(cursor, select);
                select = -1;
            } else {
                cursor = skipCharacters((GLFW.GLFW_MOD_CONTROL & modifiers) != 0, direction);
            }
        }
    }

    //#if MC >= 12109
    private com.mojang.blaze3d.platform.Window getWindow() {
        return Minecraft.getInstance().getWindow();
        //#else
        //$$ private long getWindow() {
        //$$ return Minecraft.getInstance().getWindow().getWindow();
        //#endif
    }

    private boolean hasControlDown() {
        return InputConstants.isKeyDown(getWindow(), 341) || InputConstants.isKeyDown(getWindow(), 345);
    }

    private boolean hasShiftDown() {
        return InputConstants.isKeyDown(getWindow(), 340) || InputConstants.isKeyDown(getWindow(), 344);
    }

    private boolean hasAltDown() {
        return InputConstants.isKeyDown(getWindow(), 342) || InputConstants.isKeyDown(getWindow(), 346);
    }

    private boolean isPaste(int keyCode) {
        return keyCode == 86 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    private boolean isCopy(int keyCode) {
        return keyCode == 67 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    private boolean isSelectAll(int keyCode) {
        return keyCode == 65 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    @Override
    public InputResult onKeyPressed(int ch, int key, int modifiers) {
        if (!isEditable())
            return InputResult.IGNORED;

        if (isCopy(ch)) {
            copySelection();
            return InputResult.PROCESSED;
        } else if (isPaste(ch)) {
            paste();
            return InputResult.PROCESSED;
        } else if (isSelectAll(ch)) {
            select = 0;
            cursor = text.length();
            return InputResult.PROCESSED;
        }

        switch (ch) {
        case GLFW.GLFW_KEY_DELETE -> delete(modifiers, false);
        case GLFW.GLFW_KEY_BACKSPACE -> delete(modifiers, true);
        case GLFW.GLFW_KEY_LEFT -> onDirectionalKey(-1, modifiers);
        case GLFW.GLFW_KEY_RIGHT -> onDirectionalKey(1, modifiers);
        case GLFW.GLFW_KEY_HOME, GLFW.GLFW_KEY_UP -> {
            if ((GLFW.GLFW_MOD_SHIFT & modifiers) == 0) {
                select = -1;
            }
            cursor = 0;
        }
        case GLFW.GLFW_KEY_END, GLFW.GLFW_KEY_DOWN -> {
            if ((GLFW.GLFW_MOD_SHIFT & modifiers) == 0) {
                select = -1;
            }
            cursor = text.length();
        }
        default -> {
            return InputResult.IGNORED;
        }
        }
        scrollCursorIntoView();

        return InputResult.PROCESSED;
    }

    //#if MC >= 11800
    @Override
    public void addNarrations(NarrationElementOutput builder) {
        builder.add(NarratedElementType.TITLE,
                ComponentProvider.translatable(NarrationMessages.TEXT_FIELD_TITLE_KEY, text));

        if (suggestion != null) {
            builder.add(NarratedElementType.HINT,
                    ComponentProvider.translatable(NarrationMessages.TEXT_FIELD_SUGGESTION_KEY, suggestion));
        }
    }
    //#endif
}
