import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageMatrix {
    private List<List<Color>> matrix = new ArrayList<>();

    public ImageMatrix(BufferedImage image) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);;
        for (int i = 0; i < width; i++) {
            matrix.add(new ArrayList<>());
            for (int j = 0; j < height; j++) {
                matrix.get(i).add(new Color(image.getRGB(i, j)));
            }
        }
    }

    public ImageMatrix(List<List<Color>> matrix) {
        this.matrix = matrix;
    }

    public List<List<Cell>> splitIntoCells() {
        List<List<Cell>> cells = new ArrayList<>();
        int cellRows = matrix.size() / 8;
        if (matrix.size() == 0) {
            throw new RuntimeException("Invalid image.");
        }
        int cellColumns = matrix.get(0).size() / 8;
        for (int i = 0; i < cellRows; ++i) {
            cells.add(new ArrayList<>());
            for (int j = 0; j < cellColumns; ++j) {
                Color[][] cellMatrix = new Color[8][8];
                for (int cellRow = 0; cellRow < 8; ++cellRow) {
                    for (int cellColumn = 0; cellColumn < 8; ++cellColumn) {
                        cellMatrix[cellRow][cellColumn] = matrix.get(i*8 + cellRow).get(j*8 + cellColumn);
                    }
                }
                cells.get(i).add(new Cell(cellMatrix));
            }
        }
        return cells;
    }

    public static ImageMatrix fromCells(List<List<Cell>> cells) {
        List<List<Color>> matrix = new ArrayList<>();
        for (int i = 0; i < cells.size() * 8; ++i) {
            matrix.add(new ArrayList<>());
            for (int j = 0; j < cells.get(0).size() * 8; ++j) {
                matrix.get(i).add(cells.get(i / 8).get(j / 8).matrix[i%8][j%8]);
            }
        }
        return new ImageMatrix(matrix);
    }

    public BufferedImage toBufferedImage() {
        if (matrix.size() == 0) {
            throw new RuntimeException("Invalid image.");
        }
        BufferedImage bufferedImage = new BufferedImage(matrix.size(), matrix.get(0).size(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < matrix.size(); ++i) {
            for (int j = 0; j < matrix.get(0).size(); ++j){
                bufferedImage.setRGB(i, j, matrix.get(i).get(j).getRGB());
            }
        }
        return bufferedImage;
    }
}
