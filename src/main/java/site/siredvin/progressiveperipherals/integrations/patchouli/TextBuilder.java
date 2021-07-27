package site.siredvin.progressiveperipherals.integrations.patchouli;

import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import site.siredvin.progressiveperipherals.utils.TranslationUtil;

import java.util.HashMap;
import java.util.Map;

public class TextBuilder {
    private final static String LIST_SPACING = "  ";
    private final static Map<String, String> CUSTOM_MACROS = new HashMap<String, String>() {{
        put("$(blockPos)", "$(l:progressiveperipherals:api_documentation/extra_types#BlockPos)blockPos$()");
        put("$(blockVisual)", "$(l:progressiveperipherals:api_documentation/extra_types#BlockVisual)blockVisual$()");
        put("$(configuration)", "$(l:progressiveperipherals:api_documentation/operations#configuration)getConfiguration$()");
        put("$(shapeCube)", "$(l:progressiveperipherals:api_documentation/extra_types#ShapeCube)shapeCube$()");
    }};
    private IFormattableTextComponent buffer;
    private int list_last_number = 1;
    private boolean insideList = false;

    public TextBuilder(IFormattableTextComponent start) {
        buffer = start;
    }

    public TextBuilder add(IFormattableTextComponent text) {
        buffer = buffer.append(text);
        return this;
    }

    public TextBuilder add(String text) {
        buffer = buffer.append(text);
        return this;
    }

    public TextBuilder local(String name) {
        buffer = buffer.append(TranslationUtil.localization(name));
        return this;
    }

    public TextBuilder br() {
        buffer = buffer.append("$(br)");
        return this;
    }

    public TextBuilder p() {
        buffer = buffer.append("$(p)");
        return this;
    }

    public TextBuilder bold(IFormattableTextComponent text) {
        buffer = buffer.append("$(l)").append(text).append("$()");
        return this;
    }

    public TextBuilder bold(String text) {
        buffer = buffer.append("$(l)").append(text).append("$()");
        return this;
    }

    public TextBuilder italic(IFormattableTextComponent text) {
        buffer = buffer.append("$(o)").append(text).append("$()");
        return this;
    }

    public TextBuilder italic(String text) {
        buffer = buffer.append("$(o)").append(text).append("$()");
        return this;
    }

    public TextBuilder startList() {
        if (insideList)
            throw new IllegalArgumentException("Cannot start list inside list!");
        list_last_number = 1;
        insideList = true;
        return this;
    }

    public TextBuilder finishList() {
        insideList = false;
        return br();
    }

    public ListElementBuilder listElement() {
        buffer = buffer.append(LIST_SPACING).append(String.format("%d. ", list_last_number));
        list_last_number++;
        return new ListElementBuilder(this);
    }

    public TextBuilder addBulletListElement(String name) {
        return add("$(li)").add(name);
    }

    public TextBuilder addBulletListElement(IFormattableTextComponent name) {
        return add("$(li)").add(name);
    }

    public ITextComponent build() {
        String text = buffer.getString();
        for (Map.Entry<String, String> entry: CUSTOM_MACROS.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }
        return new StringTextComponent(text);
    }

    public static TextBuilder start(IFormattableTextComponent start) {
        return new TextBuilder(start);
    }

    public static class ListElementBuilder {
        private final TextBuilder builder;

        public ListElementBuilder(TextBuilder builder) {
            this.builder = builder;
        }

        public ListElementBuilder add(String name) {
            builder.add(name);
            return this;
        }

        public ListElementBuilder add(IFormattableTextComponent component) {
            builder.add(component);
            return this;
        }

        public ListElementBuilder bold(IFormattableTextComponent text) {
            builder.bold(text);
            return this;
        }

        public ListElementBuilder bold(String text) {
            builder.bold(text);
            return this;
        }

        public ListElementBuilder italic(IFormattableTextComponent text) {
            builder.italic(text);
            return this;
        }

        public ListElementBuilder italic(String text) {
            builder.italic(text);
            return this;
        }

        public TextBuilder finish() {
            return builder.br();
        }
    }
}
