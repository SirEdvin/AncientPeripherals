package site.siredvin.progressiveperipherals.api.puzzles;

import org.squiddev.cobalt.Constants;
import org.squiddev.cobalt.LuaState;
import org.squiddev.cobalt.LuaTable;
import org.squiddev.cobalt.ValueFactory;
import org.squiddev.cobalt.function.LuaFunction;
import org.squiddev.cobalt.lib.*;
import org.squiddev.cobalt.lib.platform.VoidResourceManipulator;

public interface IPuzzle {
    String getType();
    String getDescription();
    boolean performTest(LuaFunction func);

    default Object[] execute(LuaFunction func) {
        LuaState state = LuaState.builder().resourceManipulator(new VoidResourceManipulator()).build();
        LuaTable globals = new LuaTable();
        state.setupThread(globals);
        globals.load(state, new BaseLib());
        globals.load(state, new TableLib());
        globals.load(state, new StringLib());
        globals.load(state, new MathLib());
        globals.load(state, new CoroutineLib());
        globals.load(state, new Bit32Lib());
        globals.load(state, new Utf8Lib());
        globals.load(state, new DebugLib());
        globals.rawset("collectgarbage", Constants.NIL);
        globals.rawset("dofile", Constants.NIL);
        globals.rawset("loadfile", Constants.NIL);
        globals.rawset("print", Constants.NIL);
        globals.rawset("_VERSION", ValueFactory.valueOf("Lua 5.1"));
        return new Object[0];
    }
}
