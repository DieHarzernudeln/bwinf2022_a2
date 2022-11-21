import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class Aufgabe2 {

    private int seed;
    private int width;
    private int height;
    private int crystalCount;
    private double minSpeed;
    private double maxSpeed;
    private double minColor;
    private double maxColor;

    private final int[][][] grid;
    private final double[][] sprouts;
    private int sproutPlaced;
    private final int[][] colors;
    private final Random rand;

    private final int totalPixels;
    private int filledPixels;

    public Aufgabe2() {
        loadConfig();

        JFrame frame = new JFrame("Output");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        rand = new Random(seed);
        grid = new int[height][width][];
        sprouts = new double[crystalCount][4];
        colors = new int[crystalCount][4];

        totalPixels = width * height;
        filledPixels = 0;

        getColors();
        placeAllSprouts();
        grow();

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid[y][x][1] != 4)
                    img.setRGB(x, y, colors[grid[y][x][0]][grid[y][x][1]]);
                else {
                    img.setRGB(x, y, colors[grid[y][x][0]][0]);
                }
            }
        }

        try {
            File outputFile = new File(getOutputName());
            ImageIO.write(img, "png", outputFile);
        } catch (IOException e) {
            System.out.println("Fehler beim Schreiben in die Ausgabedatei.");
        }

        JPanel panel = new JPanel() {
            public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.drawImage(img, 0, 0, null);
            }
        };

        frame.setContentPane(panel);
        frame.setSize(img.getWidth(), img.getHeight());
        frame.setVisible(true);
    }

    private void getColors() {
        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < colors[i].length; j++) {
                int c = (int) (256 * (minColor + (maxColor - minColor) * rand.nextDouble()));
                colors[i][j] = new Color(c, c, c).getRGB();
            }
        }
    }

    private void loadConfig() {
        Properties prop = new Properties();

        try {
            prop.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            System.out.println("Fehler beim Öffnen der Konfigurationsdatei.");
            System.exit(1);
        }

        seed = Integer.parseInt(prop.getProperty("seed"));
        width = Integer.parseInt(prop.getProperty("width"));
        height = Integer.parseInt(prop.getProperty("height"));
        crystalCount = Integer.parseInt(prop.getProperty("crystal_count"));
        minSpeed = Double.parseDouble(prop.getProperty("min_speed"));
        maxSpeed = Double.parseDouble(prop.getProperty("max_speed"));
        minColor = Double.parseDouble(prop.getProperty("min_color"));
        maxColor = Double.parseDouble(prop.getProperty("max_color"));
    }

    private String getOutputName() {
        return "image_" + seed + "_" +
                width + "x" + height + "_" +
                crystalCount + "_" +
                minSpeed + "-" + maxSpeed + "_" +
                minColor + "-" + maxColor +
                ".png";
    }

    private void grow() {
        boolean full = false;

        while (!full) {
            full = true;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (grid[y][x] == null) {
                        full = false;
                        continue;
                    }

                    int[] cell = grid[y][x];

                    if (cell[1] == 4)
                        continue;

                    if (rand.nextDouble() < sprouts[cell[0]][cell[1]]) {
                        switch (cell[1]) {
                            case 0 -> {
                                setCell(y - 1, x, cell);
                                setCell(y, x - 1, cell);
                                setCell(y, x + 1, cell);
                            }
                            case 1 -> {
                                setCell(y + 1, x, cell);
                                setCell(y, x - 1, cell);
                                setCell(y, x + 1, cell);
                            }
                            case 2 -> {
                                setCell(y - 1, x, cell);
                                setCell(y + 1, x, cell);
                                setCell(y, x - 1, cell);
                            }
                            case 3 -> {
                                setCell(y - 1, x, cell);
                                setCell(y + 1, x, cell);
                                setCell(y, x + 1, cell);
                            }
                        }
                    }
                }
            }

            System.out.println(((float)filledPixels / totalPixels * 100) + "%");
        }
    }

    private void placeAllSprouts() {
        while (sproutPlaced < crystalCount) {
            int y = rand.nextInt(height);
            int x = rand.nextInt(width);

            if (grid[y][x] != null) {
                if (grid[y][x][1] != 4) {
                    continue;
                }
            }

            addSprout(y, x);
            sproutPlaced++;
        }
    }

    private void addSprout(int y, int x) {
        for (int i = 0; i < 4; i++) {
            if (minSpeed >= maxSpeed)
                sprouts[sproutPlaced][i] = minSpeed;
            else
                sprouts[sproutPlaced][i] = minSpeed + ((maxSpeed - minSpeed) * rand.nextDouble());
        }

        setCell(y, x, new int[]{sproutPlaced, 4});
        setCell(y - 1, x, new int[]{sproutPlaced, 0});
        setCell(y + 1, x, new int[]{sproutPlaced, 1});
        setCell(y, x - 1, new int[]{sproutPlaced, 2});
        setCell(y, x + 1, new int[]{sproutPlaced, 3});
    }

    private void setCell(int y, int x, int[] cell) {
        if (y >= 0 && x >= 0 && y < height && x < width)
            if (grid[y][x] == null) {
                grid[y][x] = cell;

                filledPixels++;
            }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Aufgabe2();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
