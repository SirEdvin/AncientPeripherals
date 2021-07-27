package site.siredvin.progressiveperipherals.common.puzzles;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import org.ejml.data.SingularMatrixException;
import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.api.puzzles.IPuzzleTask;
import site.siredvin.progressiveperipherals.utils.LuaUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LinearSystemDescription implements IPuzzleTask {
    private static final double MIN_VALUE = -5000;
    private static final double MAX_VALUE = 5000;
    private static final double ALLOWED_ERROR = 1e-8;

    private final SimpleMatrix A;
    private final SimpleMatrix b;

    public LinearSystemDescription(SimpleMatrix A, SimpleMatrix b) {
        this.A = A;
        this.b = b;
    }

    public SimpleMatrix getA() {
        return A;
    }

    public SimpleMatrix getB() {
        return b;
    }

    public @Nullable SimpleMatrix solve() {
        try {
            return A.solve(b);
        } catch (SingularMatrixException e) {
            return null;
        }
    }

    public static LinearSystemDescription generate(int size, Random random) {
        SimpleMatrix A = SimpleMatrix.random_DDRM(size, size, MIN_VALUE, MAX_VALUE, random);
        while (A.determinant() == 0) {
            A = SimpleMatrix.random_DDRM(size, size, MIN_VALUE, MAX_VALUE, random);
        }
        return new LinearSystemDescription(A, SimpleMatrix.random_DDRM(size, 1, MIN_VALUE, MAX_VALUE, random));
    }

    @Override
    public @NotNull Map<String, Object> getLuaDescription() {
        Map<String, Object> data = new HashMap<>();
        data.put("b", LuaUtils.toLua(b.getDDRM().getData()));

        double[][] Adata = new double[A.numRows()][A.numCols()];
        for (int row = 0; row < A.numRows(); row++) {
            for (int column = 0; column < A.numCols(); column++) {
                Adata[row][column] = A.get(row, column);
            }
        }

        data.put("A", LuaUtils.toLua(Adata));
        return data;
    }

    @Override
    public boolean checkSolution(@NotNull IArguments arguments) throws LuaException {
        Map<?, ?> data = arguments.getTable(0);
        double[] suggestedSolution = LuaUtils.toArray((Map<Double, Number>) data);
        return A.mult(new SimpleMatrix(4, 1, true, suggestedSolution)).minus(b).elementSum() < ALLOWED_ERROR;
    }

    @Override
    public int getTimeLimitInSeconds() {
        // TODO: settings
        return 2;
    }
}
