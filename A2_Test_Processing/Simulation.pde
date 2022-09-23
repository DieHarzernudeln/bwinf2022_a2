void starteSimulation() { //<>//
    simulationGestartet = true;

    img = createImage(width, height, RGB);

    for (PVector keim : keime) {
        erstelleKeim((int)keim.x, (int)keim.y);
    }
}

void erstelleKeim(int x, int y) {
    if (x < width - 1) {
        img.set(x + 1, y, FARBE_RECHTS);
        gefuelltePixel++;
    }
    if (y < height - 1) {
        img.set(x, y + 1, FARBE_UNTEN);
        gefuelltePixel++;
    }
    if (x > 0) {
        img.set(x - 1, y, FARBE_LINKS);
        gefuelltePixel++;
    }
    if (y > 0) {
        img.set(x, y - 1, FARBE_OBEN);
        gefuelltePixel++;
    }
}

void simulationsSchritt() {
    if (gefuelltePixel < width * height) {
        img.loadPixels();

        int gefuelltesPixel = (int)random(0, gefuelltePixel);

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                if ((img.pixels[y * width + x] & 0xFFFFFF) == 0)
                    continue;

                if (gefuelltesPixel > 0) {
                    gefuelltesPixel--;
                    continue;
                }

                color farbe = img.pixels[y * width + x];

                if (x < width - 1 && (img.pixels[y * width + x + 1] & 0xFFFFFF) == 0) {
                    img.pixels[y * width + x + 1] = farbe;
                    gefuelltePixel++;
                }
                if (y < height - 1 && (img.pixels[(y + 1) * width + x] & 0xFFFFFF) == 0) {
                    img.pixels[(y + 1) * width + x] = farbe;
                    gefuelltePixel++;
                }
                if (x > 0 && (img.pixels[y * width + x - 1] & 0xFFFFFF) == 0) {
                    img.pixels[y * width + x - 1] = farbe;
                    gefuelltePixel++;
                }
                if (y > 0 && (img.pixels[(y - 1) * width + x] & 0xFFFFFF) == 0) {
                    img.pixels[(y - 1) * width + x] = farbe;
                    gefuelltePixel++;
                }

                y = height;
                x = width;
            }

        img.updatePixels();
    }
}
