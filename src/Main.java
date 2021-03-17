import parcs.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        task curtask = new task();
        curtask.addJarFile("Runner.jar");
        AMInfo info = new AMInfo(curtask, null);

        System.out.println("Loading image...");
        File img = new File(curtask.findFile("image.jpg"));
        BufferedImage in = ImageIO.read(img);
        BufferedImage newImage = new BufferedImage(
                in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_RGB);
        File outputfile2 = new File("processedImageTest.jpg");
        ImageIO.write(newImage, "jpg", outputfile2);
        ImageMatrix imageMatrix = new ImageMatrix(newImage);

        List<List<Cell>> cells = imageMatrix.splitIntoCells();

        List<channel> channels = new ArrayList<>();
        for (List<Cell> cellRow: cells) {
            point p = info.createPoint();
            channel c = p.createChannel();
            p.execute("Runner");
            c.write((Serializable) cellRow);
            channels.add(c);
        }

        System.out.println("Waiting for result...");

        List<List<Cell>> processedCells = new ArrayList<>();
        for (parcs.channel channel : channels) {
            List<Cell> cellRow = (List<Cell>) channel.readObject();
            processedCells.add(cellRow);
        }

        System.out.println("Decoding image.");
        BufferedImage processed = ImageMatrix.fromCells(processedCells).toBufferedImage();
        File outputfile = new File("processedImage.jpg");
        ImageIO.write(processed, "jpg", outputfile);
        System.out.println("Done.");
        curtask.end();
    }
}
