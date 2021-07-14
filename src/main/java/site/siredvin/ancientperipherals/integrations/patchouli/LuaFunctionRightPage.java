package site.siredvin.ancientperipherals.integrations.patchouli;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

import java.util.List;

public class LuaFunctionRightPage extends BookPage {

    IVariable parameters;
    IVariable returns;

    transient BookTextRenderer textRenderer;

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);

        TextBuilder builder = TextBuilder.start(new StringTextComponent(""));
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
        textRenderer = new BookTextRenderer(parent, builder.build(), 0, LuaFunctionPage.STARTING_HEIGHT_WITHOUT_TITLE);

    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float pticks) {
        textRenderer.render(ms, mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, int mouseButton) {
        return textRenderer.click(mouseX, mouseY, mouseButton);
    }
}
