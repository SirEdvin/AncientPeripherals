package site.siredvin.progressiveperipherals.integrations.patchouli;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.common.configuration.ProgressivePeripheralsConfig;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.gui.BookTextRenderer;
import vazkii.patchouli.client.book.gui.GuiBookEntry;

import java.util.List;

public class LuaFunctionRightPage extends BookPage {

    @Nullable IVariable parameters;
    @Nullable IVariable operationReturn;
    @Nullable IVariable checkReturn;
    @Nullable IVariable returns;

    transient BookTextRenderer textRenderer;

    @Override
    public void onDisplayed(GuiBookEntry parent, int left, int top) {
        super.onDisplayed(parent, left, top);

        TextBuilder builder = TextBuilder.start(new StringTextComponent(""));
        if (parameters != null) {
            builder.local("parameters").br().startList();
            List<IVariable> parameterList = parameters.asList();
            for (IVariable parameter: parameterList) {
                List<IVariable> parameterArray = parameter.asList();
                IFormattableTextComponent firstElement = (IFormattableTextComponent) parameterArray.get(0).as(ITextComponent.class);
                TextBuilder.ListElementBuilder listElementBuilder;
                if (firstElement.getString().equals("...")) {
                    listElementBuilder = builder.prefixElement(firstElement);
                } else {
                    listElementBuilder = builder.listElement().italic(firstElement);
                }
                listElementBuilder.add(": ")
                        .add((IFormattableTextComponent) parameterArray.get(1).as(ITextComponent.class))
                        .finish();
            }
            builder.finishList();
        }
        if (operationReturn != null && operationReturn.asBoolean()) {
            /*
            ["true | nil", "True if operation successful, nil otherwise"],
            ["nil | string", "Error message"]
             */
            builder.local("returns").br().startList();
            builder.listElement().italic("true | nil").add(": ").add("True if operation successful, nil otherwise").finish();
            builder.listElement().italic("nil | string").add(": ").add("Error message").finish();
        } else if (checkReturn != null) {
            /*
            ["true | nil", "True if <>, nil otherwise"],
            ["nil | string", "Reason, why not <>"]
             */
            builder.local("returns").br().startList();
            builder.listElement().italic("true | nil").add(": ").add(String.format("True if %s, nil otherwise", checkReturn.asString())).finish();
            builder.listElement().italic("nil | string").add(": ").add(String.format("Reason, why not %s", checkReturn.asString())).finish();
        } else if (returns != null) {
            List<IVariable> returns = this.returns.asList();
            builder.local("returns").br().startList();
            for (IVariable returnValue : returns) {
                List<IVariable> returnArray = returnValue.asList();
                builder.listElement().italic((IFormattableTextComponent) returnArray.get(0).as(ITextComponent.class))
                        .add(": ")
                        .add((IFormattableTextComponent) returnArray.get(1).as(ITextComponent.class))
                        .finish();
            }
            builder.finishList();
        } else if (ProgressivePeripheralsConfig.strictPatchouli)
            throw new IllegalArgumentException(String.format("Incorrect filled page %d inside entry %s", this.pageNum, this.entry.getId()));
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
