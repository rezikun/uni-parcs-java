import parcs.*;

import javax.imageio.ImageIO;
import java.awt.*;
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
        BufferedImage newImage = ImageIO.read(img);
        System.out.println("OLD");
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j){
                Color color = new Color(newImage.getRGB(i, j));
                System.out.println(String.format("A: %d, R: %d G: %d B: %d",
                        color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()));
            }
        }
        System.out.println("\n");
        ImageMatrix imageMatrix = new ImageMatrix(newImage);

        List<List<Cell>> cells = imageMatrix.splitIntoCells();
        BufferedImage processedTest = ImageMatrix.fromCells(cells).toBufferedImage();
        File outputfileTest = new File("processedImageTest.jpg");
        ImageIO.write(processedTest, "jpg", outputfileTest);

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
        System.out.println("NEW");
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j){
                Color color = new Color(processed.getRGB(i, j));
                System.out.println(String.format("A: %d, R: %d G: %d B: %d",
                        color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()));
            }
        }
        System.out.println("\n");
        File outputfile = new File("processedImage.jpg");
        ImageIO.write(processed, "jpg", outputfile);
        System.out.println("Done.");
        curtask.end();
    }
}
