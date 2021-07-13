package site.siredvin.ancientperipherals.integrations.patchouli;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import site.siredvin.ancientperipherals.utils.TranslationUtil;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

import java.util.ArrayList;
import java.util.List;

public class LuaFunctionPage extends BookPage {

    private static final int STARTING_HEIGHT = 24;

    IVariable parameters;
    IVariable output;
    IVariable canOutputError;
    IVariable functionName;
    IVariable description;

    transient ITextComponent actualTitle;
    transient List<BookTextRenderer> parameterRenderers;
    transient BookTextRenderer canOutputErrorRenderer;
    transient BookTextRenderer outputRenderer;
    transient BookTextRenderer descriptionRenderer;

    private StringTextComponent prefixedLocalizedText(String prefix, IVariable text) {
        // Well, if you just pass text component after `append`
        // it just ignored
        return new StringTextComponent(TranslationUtil.localization(prefix).append(text.as(ITextComponent.class)).getString());
    }

    private StringTextComponent prefixedText(String prefix, IVariable text) {
        // Well, if you just pass text component after `append`
        // it just ignored
        return new StringTextComponent(new StringTextComponent(prefix).append(text.as(ITextComponent.class)).getString());
    }

    private StringTextComponent wrapParameter(IVariable parameter) {
        List<IVariable> parameterData = parameter.asList();
        IFormattableTextComponent builder = new StringTextComponent("  - $(l)");
        builder = builder.append(parameterData.get(0).as(ITextComponent.class));
        builder = builder.append("$() ");
        builder = builder.append(parameterData.get(1).as(ITextComponent.class));
        return new StringTextComponent(builder.getString());
    }

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);

        actualTitle = functionName.as(ITextComponent.class);

        parameterRenderers = new ArrayList<>();
        int height_counter = STARTING_HEIGHT;
        parameterRenderers.add(new BookTextRenderer(parent, TranslationUtil.localization("parameters"), 0, height_counter));
        height_counter += GuiBook.TEXT_LINE_HEIGHT;

        for (IVariable parameter: parameters.asList()) {
            parameterRenderers.add(new BookTextRenderer(parent, wrapParameter(parameter), 0, height_counter));
            height_counter += GuiBook.TEXT_LINE_HEIGHT;
        }
        outputRenderer = new BookTextRenderer(parent, prefixedLocalizedText("output", output), 0, height_counter);
        height_counter += GuiBook.TEXT_LINE_HEIGHT;
        canOutputErrorRenderer = new BookTextRenderer(parent, prefixedLocalizedText("can_output_error", canOutputError), 0, height_counter);
        height_counter += GuiBook.TEXT_LINE_HEIGHT;
        descriptionRenderer = new BookTextRenderer(parent, prefixedLocalizedText("description", description), 0, height_counter);

    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
        parent.drawCenteredStringNoShadow(ms, i18n(actualTitle.getString()), GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
        GuiBook.drawSeparator(ms, book, 0, 12);
        parameterRenderers.forEach(bookTextRenderer -> bookTextRenderer.render(ms, mouseX, mouseY));
        outputRenderer.render(ms, mouseX, mouseY);
        canOutputErrorRenderer.render(ms, mouseX, mouseY);
        descriptionRenderer.render(ms, mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, int mouseButton) {
        if (descriptionRenderer.click(mouseX, mouseY, mouseButton)) {
            return true;
        }
        if (canOutputErrorRenderer.click(mouseX, mouseY, mouseButton)) {
            return true;
        }
        if (outputRenderer.click(mouseX, mouseY, mouseButton)){
            return true;
        }
        return parameterRenderers.stream().map(bookTextRenderer -> bookTextRenderer.click(mouseX, mouseY, mouseButton)).reduce((x, y) -> x || y).orElse(false);
    }
}
