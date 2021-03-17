import parcs.AM;
import parcs.AMInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Runner implements AM {
    @Override
    public void run(AMInfo info) {
        List<Cell> row = (List<Cell>) info.parent.readObject();
        System.out.println(String.format("Got row of size %d", row.size()));
        List<Cell> newRow = new ArrayList<>();
        for (Cell cell : row) {
            System.out.println(String.format("Before %d", cell.matrix[0][0].getBlue()));
            float[][] rMatrix = new float[8][8];
            float[][] gMatrix = new float[8][8];
            float[][] bMatrix = new float[8][8];
            cell.transform(rMatrix, gMatrix, bMatrix);
            cell = Cell.detransform(rMatrix, gMatrix, bMatrix);
            System.out.println(String.format("After %d", cell.matrix[0][0].getBlue());
            newRow.add(cell);
        }
        info.parent.write((Serializable) newRow);
    }
}
