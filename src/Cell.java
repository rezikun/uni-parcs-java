import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

public class Cell implements Serializable {
    Color[][] matrix;
    private static final long serialVersionUID = -1779337317625099010L;
    private static final int[][] quantizationTable = {
            {17*20, 18*20, 24*20, 47*20, 99*20, 99*20, 99*20, 99*20},
            {18*20, 21*20, 26*20, 66*20, 99*20, 99*20, 99*20, 99*20},
            {24*20, 26*20, 56*20, 99*20, 99*20, 99*20, 99*20, 99*20},
            {47*20, 66*20, 99*20, 99*20, 99*20, 99*20, 99*20, 99*20},
            {99*20, 99*20, 99*20, 99*20, 99*20, 99*20, 99*20, 99*20},
            {99*20, 99*20, 99*20, 99*20, 99*20, 99*20, 99*20, 99*20},
            {99*20, 99*20, 99*20, 99*20, 99*20, 99*20, 99*20, 99*20},
            {99*20, 99*20, 99*20, 99*20, 99*20, 99*20, 99*20, 99*20}};

    private static final float THRESHOLD = 0.0000005f;
    public Cell(Color[][] cell) {
        matrix = cell;
    }

    private static float useQuatization(float val, int i, int j) {
        float res = val / quantizationTable[i][j];
        return Math.abs(res) > THRESHOLD ? res : 0.0f;
    }

    private static float invertQuatization(float val, int i, int j) {
        return val * quantizationTable[i][j];
    }
    public static float[] mode(float[][] arr) {
        List<Float> list = new ArrayList<Float>();
        for (float[] ints : arr) {
            for (float anInt : ints) {
                list.add(anInt);
            }
        }
        float[] vector = new float[list.size()];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = list.get(i);
        }
        return vector;
    }
    public void transform(float[][] rMatrix, float[][] gMatrix, float[][] bMatrix) {
        for (int i = 0; i< 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                rMatrix[i][j] = matrix[i][j].getRed() - 128;
                gMatrix[i][j] = matrix[i][j].getGreen() - 128;
                bMatrix[i][j] = matrix[i][j].getBlue() - 128;
            }
        }
        Dct.forwardDCT8x8(mode(rMatrix));
        Dct.forwardDCT8x8(mode(gMatrix));
        Dct.forwardDCT8x8(mode(bMatrix));
        Dct.forwardDCT8x8(mode(rMatrix));
        Dct.forwardDCT8x8(mode(gMatrix));
        Dct.forwardDCT8x8(mode(bMatrix));
        for (int i = 0; i< 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                rMatrix[i][j] = useQuatization(rMatrix[i][j], i, j);
                gMatrix[i][j] = useQuatization(gMatrix[i][j], i, j);
                bMatrix[i][j] = useQuatization(bMatrix[i][j], i, j);
            }
        }
    }

    public static Cell detransform(float[][] rMatrix, float[][] gMatrix, float[][] bMatrix) {
        for (int i = 0; i< 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                rMatrix[i][j] = invertQuatization(rMatrix[i][j], i, j);
                gMatrix[i][j] = invertQuatization(gMatrix[i][j], i, j);
                bMatrix[i][j] = invertQuatization(bMatrix[i][j], i, j);
            }
        }
        Dct.inverseDCT8x8(mode(rMatrix));
        Dct.inverseDCT8x8(mode(gMatrix));
        Dct.inverseDCT8x8(mode(bMatrix));
        Dct.inverseDCT8x8(mode(rMatrix));
        Dct.inverseDCT8x8(mode(gMatrix));
        Dct.inverseDCT8x8(mode(bMatrix));
        Color[][] newMatrix = new Color[8][8];
        for (int i = 0; i< 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                newMatrix[i][j] =
                        new Color((int)rMatrix[i][j] + 128, (int)gMatrix[i][j] + 128, (int)bMatrix[i][j] + 128);
            }
        }
        return new Cell(newMatrix);
    }

    public void encode(List<Float> rvector, List<Float> gvector, List<Float> bvector) {
        float[][] rMatrix = new float[8][8];
        float[][] gMatrix = new float[8][8];
        float[][] bMatrix = new float[8][8];
        transform(rMatrix, gMatrix, bMatrix);
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                rvector.add(rMatrix[i][j]);
                gvector.add(gMatrix[i][j]);
                bvector.add(bMatrix[i][j]);
            }
        }
    }

    public static Cell decode(List<Float> rvector, List<Float> gvector, List<Float> bvector) {
        float[][] rMatrix = new float[8][8];
        float[][] gMatrix = new float[8][8];
        float[][] bMatrix = new float[8][8];
        for (int i = 0; i < rvector.size(); ++i) {
            rMatrix[i / 8][i % 8] = rvector.get(i);
            gMatrix[i / 8][i % 8] = gvector.get(i);
            bMatrix[i / 8][i % 8] = bvector.get(i);
        }

        return detransform(rMatrix, gMatrix, bMatrix);
    }
}
