import parcs.AM;
import parcs.AMInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DCT implements AM {
    @Override
    public void run(AMInfo info) {
        int width = info.parent.readInt();
        List<Cell> row = (List<Cell>) info.parent.readObject();
        List<Cell> newRow = new ArrayList<>();
        for (Cell cell : row) {
            newRow.add(Cell.decode(cell.encode()));
        }
        info.parent.write((Serializable) newRow);
    }
}
