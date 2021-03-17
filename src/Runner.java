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
            List<Float> rvector = new ArrayList<>();
            List<Float> gvector = new ArrayList<>();
            List<Float> bvector = new ArrayList<>();
            cell.encode(rvector, gvector, bvector);
            newRow.add(Cell.decode(rvector, gvector, bvector));
        }
        info.parent.write((Serializable) newRow);
    }
}
