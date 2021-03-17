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
        System.out.println(String.format("Got row of size %d", row.size()));
        List<Cell> newRow = new ArrayList<>();
        for (Cell cell : row) {
            newRow.add(Cell.decode(cell.encode()));
        }
        info.parent.write((Serializable) newRow);
    }
}
