public class Medium6 {

    final double speed = 340;
    final int boxsize = 256;
    final int raynum = 10000;

    private double frequency;
    private double amplitude = .5;
    private double centerX = 0;
    private double centerY = 0;
    private double lambda;
    private double wavenumber;
    private double decay = .9;
    private double r = .5;

    private double[][] data;

    public Medium6(double frequency1) {
        frequency = frequency1;
        StdOut.println(frequency);
        data = new double[2 * boxsize + 1][2 * boxsize + 1];
        lambda = speed / frequency;
        wavenumber = 2 * Math.PI / lambda;
    }

    public boolean isonboundary(double x, double y) {
        if (Math.abs((int) x) >= boxsize) {
            return true;
        }
        if (Math.abs((int) y) >= boxsize) {
            return true;
        }
        else {
            return false;
        }
    }

    public double[] reflect(double x, double y, double xcomp0, double ycomp0) {
        double xcomp = xcomp0;
        double ycomp = ycomp0;
        double[] anglecomps = new double[2];
        if (Math.abs((int) x) == boxsize) {
            xcomp *= -1;
        }
        if (Math.abs((int) y) == boxsize) {
            ycomp *= -1;
        }
        anglecomps[0] = xcomp;
        anglecomps[1] = ycomp;
        return anglecomps;
    }

    public void ray(double x, double y, double angle, double amplitude) {
        double positionx = x;
        double positiony = y;
        double amp0 = amplitude;
        double amp = 0;
        double distance = 0;
        double directionx = Math.cos(angle);
        double directiony = Math.sin(angle);
        while (amp0 > .1) {
            data[(int) positionx + boxsize][(int) positiony + boxsize] += amp;
            positionx += directionx;
            positiony += directiony;
            distance++;
            amp = amp0 * Math.cos(wavenumber * distance);
            if (isonboundary(positionx, positiony)) {
                double[] anglecomps = reflect(positionx, positiony, directionx,
                        directiony);
                if (directionx != anglecomps[0]) {
                    directionx = anglecomps[0];
                    positionx += 2 * directionx;
                }
                if (directiony != anglecomps[1]) {
                    directiony = anglecomps[1];
                    positiony += 2 * directiony;
                }
                amp0 = decay * amp;
            }
        }
    }

    public void draw(double[][] data) {
        StdDraw.show(0);
        StdDraw.setXscale(-boxsize, boxsize);
        StdDraw.setYscale(-boxsize, boxsize);
        //double grayscale;
        for (int i = -boxsize; i < boxsize; i++) {
            for (int j = -boxsize; j < boxsize; j++) {
                if (Math.abs(data[i + boxsize][j + boxsize]) < .05) {
                    StdDraw.setPenColor(StdDraw.WHITE);
                }
                else {
                    StdDraw.setPenColor();
                }
                StdDraw.filledSquare(i, j, r);
            }
        }
        StdDraw.show(0);
    }

    public static void main(String[] args) {
        for (double frequency = 1.2; frequency < 20; frequency += .05) {
            Medium6 medium = new Medium6(frequency);
            double increment = 2 * Math.PI / medium.raynum;
            double angle = 0;
            for (int i = 0; i < medium.raynum; i++) {
                medium.ray(medium.centerX, medium.centerY, angle,
                        medium.amplitude);
                angle += increment;
            }
            medium.draw(medium.data);
        }
    }
}