public class Medium2 {

    final double speed = 1;
    final int boxsize = 256;
    final int raynum = 300;

    private double frequency;
    private double amplitude = .5;
    private double centerX = 0;
    private double centerY = 0;
    private double lambda;
    private double wavenumber;

    private double decay;
    
    private double[][] data;

    public Medium2(double frequency1) {
        frequency = frequency1;
        data = new double[2 * boxsize + 1][2 * boxsize + 1];
        lambda = speed / frequency;
        wavenumber = 2 * Math.PI / lambda;
    }

    public boolean isonboundary(double x, double y) {
        if (Math.abs(x) >= boxsize) {
            return true;
        }
        else if (Math.abs(y) >= boxsize) {
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
        if (Math.abs(x) == boxsize) {
            xcomp *= -1;
        }
        else if (Math.abs(y) == boxsize) {
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
        double phase = 0;
        int adjustx;
        int adjusty;
        while (amp0 > .1) {
            adjustx = (x < 0) ? 1 : 0;
            adjusty = (y < 0) ? 1 : 0;
            
            adjustx = 0;
            adjusty = 0;
            System.out.println(positionx);
            data[(int) Math.floor(positionx) + adjustx + boxsize][(int) Math
                    .floor(positiony) + adjusty + boxsize] += amp;
            positionx += directionx;
            positiony += directiony;
            distance += Math.sqrt((directionx * directionx)
                    + (directiony * directiony));
            amp = amp0 * Math.sin(wavenumber * distance + phase);
            //System.out.println(amp);
            //System.out.println("DEBUG A");
            //waittime(10);
            if (isonboundary(positionx, positiony)) {
                System.out.println("DEBUG B");
                double[] anglecomps = reflect(positionx, positiony, directionx,
                        directiony);
                directionx = anglecomps[0];
                directiony = anglecomps[1];
                amp0 = amp;
                phase = Math.PI / 2;
            }
        }
    }

    private void waittime(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public void draw(double[][] data) {
        System.out.println("drawing");
        StdDraw.show(0);
        StdDraw.setXscale(-boxsize, boxsize);
        StdDraw.setYscale(-boxsize, boxsize);
        double grayscale;
        for (int i = -boxsize; i < boxsize; i++) {
            System.out.println(i);
            for (int j = -boxsize; j < boxsize; j++) {
                grayscale = 255 * data[i + boxsize][j + boxsize] + 128;
                System.out.println(grayscale);
                
                StdDraw.setPenColor((int) Math.rint(grayscale),
                        (int) Math.rint(grayscale), (int) Math.rint(grayscale));
                        
                //StdDraw.setPenColor((int) grayscale, (int) grayscale, (int) grayscale);
                
                //StdDraw.point(i, j);
                StdDraw.filledSquare(i, j, .5);
            }
        }
        StdDraw.show(0);
    }

    public static void main(String[] args) {
        //double frequency = Double.parseDouble(args[0]);
        double frequency = .1666;
        Medium2 medium = new Medium2(frequency);

        //medium.draw(medium.data);
        
        
        double increment = 2 * Math.PI / medium.raynum;
        double angle = 0;
        for (int i = 0; i < medium.raynum; i++) {
            medium.ray(medium.centerX, medium.centerY, angle, medium.amplitude);
            angle += increment;
        }
        medium.draw(medium.data);
        

    }
}