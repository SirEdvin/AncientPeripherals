package site.siredvin.ancientperipherals.integrations.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

import java.util.List;

public class LuaFunctionPage extends BookPage {

    protected static final int STARTING_HEIGHT = 24;
    protected static final int STARTING_HEIGHT_WITHOUT_TITLE = 9;

    IVariable parameters;
    IVariable returns;
    @SerializedName("throw") IVariable can_throw;
    IVariable functionName;
    IVariable description;

    transient ITextComponent actualTitle;
    transient BookTextRenderer textRenderer;

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);
        actualTitle = functionName.as(ITextComponent.class);

        TextBuilder builder = TextBuilder.start((IFormattableTextComponent) description.as(ITextComponent.class)).p();
        if (parameters != null) {
            builder.addLocal("parameters").br().startList();
            List<IVariable> parameterList = parameters.asList();
            for (IVariable parameter: parameterList) {
                List<IVariable> parameterArray = parameter.asList();
                builder.listElement().italic((IFormattableTextComponent) parameterArray.get(0).as(ITextComponent.class))
                        .add(": ")
                        .add((IFormattableTextComponent) parameterArray.get(1).as(ITextComponent.class))
                        .finish();
            }
            builder.finishList();
        }
        List<IVariable> returns = this.returns.asList();
        builder.addLocal("returns").br().startList();
        for (IVariable returnValue: returns) {
            List<IVariable> returnArray = returnValue.asList();
            builder.listElement().italic((IFormattableTextComponent) returnArray.get(0).as(ITextComponent.class))
                    .add(": ")
                    .add((IFormattableTextComponent) returnArray.get(1).as(ITextComponent.class))
                    .finish();
        }
        builder.finishList();
        if (can_throw != null) {
            builder.addLocal("throw").br();
            List<IVariable> throwReasons = can_throw.asList();
            for (IVariable throwReason: throwReasons) {
                builder.addBulletListElement((IFormattableTextComponent) throwReason.as(ITextComponent.class));
            }
        }
        textRenderer = new BookTextRenderer(parent, builder.build(), 0, STARTING_HEIGHT);

    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
        parent.drawCenteredStringNoShadow(ms, i18n(actualTitle.getString()), GuiBook.PAGE_WIDTH / 2, 0, book.headerColor);
        GuiBook.drawSeparator(ms, book, 0, 12);
        textRenderer.render(ms, mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, int mouseButton) {
        return textRenderer.click(mouseX, mouseY, mouseButton);
    }
}
