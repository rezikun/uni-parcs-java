import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import org.jtransforms.dct.*;

public class Cell implements Serializable {
    Color[][] matrix = new Color[8][8];

    private static final int[][] quantizationTable = {
            {17, 18, 24, 47, 99, 99, 99, 99},
            {18, 21, 26, 66, 99, 99, 99, 99},
            {24, 26, 56, 99, 99, 99, 99, 99},
            {47, 66, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99}};

    private static final float THRESHOLD = 0.0005f;
    public Cell(Color[][] cell) {
        matrix = cell;
    }

    private float useQuatization(float val, int i, int j) {
        float res = val / quantizationTable[i][j];
        return Math.abs(res) > THRESHOLD ? res : 0.0f;
    }

    private float invertQuatization(float val, int i, int j) {
        return val * quantizationTable[i][j];
    }

    private Cell transform() {
        float[][] rMatrix = new float[8][8];
        float[][] gMatrix = new float[8][8];
        float[][] bMatrix = new float[8][8];
        for (int i = 0; i< 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                rMatrix[i][j] = matrix[i][j].getRed() - 128;
                gMatrix[i][j] = matrix[i][j].getGreen() - 128;
                bMatrix[i][j] = matrix[i][j].getBlue() - 128;
            }
        }
        FloatDCT_2D transformer = new FloatDCT_2D(8, 8);
        transformer.forward(rMatrix, false);
        transformer.forward(gMatrix, false);
        transformer.forward(bMatrix, false);
        Color[][] newMatrix = new Color[8][8];
        for (int i = 0; i< 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                newMatrix[i][j] = new Color(useQuatization(rMatrix[i][j], i, j), useQuatization(gMatrix[i][j], i, j),
                        useQuatization(bMatrix[i][j], i, j));
            }
        }
        return new Cell(newMatrix);
    }

    private Cell detransform() {
        float[][] rMatrix = new float[8][8];
        float[][] gMatrix = new float[8][8];
        float[][] bMatrix = new float[8][8];
        for (int i = 0; i< 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                rMatrix[i][j] = invertQuatization(matrix[i][j].getRed(), i, j);
                gMatrix[i][j] = invertQuatization(matrix[i][j].getGreen(), i, j);
                bMatrix[i][j] = invertQuatization(matrix[i][j].getBlue(), i, j);
            }
        }
        FloatDCT_2D transformer = new FloatDCT_2D(8, 8);
        transformer.inverse(rMatrix, false);
        transformer.inverse(gMatrix, false);
        transformer.inverse(bMatrix, false);
        Color[][] newMatrix = new Color[8][8];
        for (int i = 0; i< 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                newMatrix[i][j] = new Color(rMatrix[i][j] + 128, gMatrix[i][j] + 128, bMatrix[i][j] + 128);
            }
        }
        return new Cell(newMatrix);
    }

    public List<Color> encode() {
        Cell transformedCell = this.transform();
        List<Color> vector = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            vector.addAll(Arrays.asList(transformedCell.matrix[i]).subList(0, 8));
        }
        return vector;
    }

    public static Cell decode(List<Color> vector) {
        Color[][] mat = new Color[8][8];
        for (int i = 0; i < vector.size(); ++i) {
            mat[i / 8][i % 8] = vector.get(i);
        }
        return new Cell(mat);
    }
}
