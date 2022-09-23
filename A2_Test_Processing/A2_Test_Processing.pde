PImage img = null;

ArrayList<PVector> keime = new ArrayList<PVector>();
PVector ausgewaehlterKeim = null;

boolean simulationGestartet = false;
boolean keimeSichtbar = true;
int gefuelltePixel = 0;

void setup() {
    size(205, 154);
}

void draw() {
    if (ausgewaehlterKeim != null && !simulationGestartet) {
        ausgewaehlterKeim.x = mouseX;
        ausgewaehlterKeim.y = mouseY;
    }

    background(255);

    if (img != null) {
        image(img, 0, 0, width, height);
    }

    if (keimeSichtbar)
        for (PVector keim : keime) {
            noStroke();
            fill(255, 0, 0);
            ellipse(keim.x, keim.y, 20, 20);
        }

    if (simulationGestartet)
        for (int i = 0; i < 100000; i++)
            simulationsSchritt();
}

void mousePressed() {
    if (mouseButton == LEFT && !simulationGestartet) {
        for (PVector keim : keime) {
            float dX = mouseX - keim.x;
            float dY = mouseY - keim.y;

            if (dX * dX + dY * dY < 10 * 10) {
                ausgewaehlterKeim = keim;
                break;
            }
        }

        if (ausgewaehlterKeim == null) {
            PVector neuerKeim = new PVector(mouseX, mouseY);
            keime.add(neuerKeim);
            ausgewaehlterKeim = neuerKeim;
        }
    } else if (mouseButton == RIGHT && !simulationGestartet) {
        for (PVector keim : keime) {
            float dX = mouseX - keim.x;
            float dY = mouseY - keim.y;

            if (dX * dX + dY * dY < 20 * 20) {
                keime.remove(keim);
                break;
            }
        }
    }
}

void mouseReleased() {
    ausgewaehlterKeim = null;
}

void keyPressed() {
    if (key == ' ') {
        if (!simulationGestartet)
            starteSimulation();
        else
            keimeSichtbar = !keimeSichtbar;
    }
}
