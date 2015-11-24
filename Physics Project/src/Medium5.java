import java.awt.Color;

public class Medium5 {
    final double speed = 1;
    
    static final double maxX = 1;

    final static double TwoPI = 2 * Math.PI;
    final static int rays = 1024;
    final static int cells = 256;
    final static double step = 1.0 / cells;
    static double decay = .8;
    static double distDecay = 1;

    private static final double THRESHHOLD = .01;

    private static final double MAX_COLOR = .8;

    private double frequency;
    private double amplitudeInit = 1;
    private double centerX = maxX/2;
    private double centerY = .5000;
    
    
    private int rayCount = 0;

    private double omega;

    // we also need some way to define the edges
    // maybe an array that holds a bunch of lines?
    // what about curved edges? probably way more than we need to do
    // we may want another class for the edges

    // this holds the size of the vibrations at each point
    // the 2D array corresponds to points on the plate
    // basically the whole program is trying to fill this in
    private double[][] data;

    // probably needs to initialize edges and stuff too
    public Medium5(double frequency1) {
        frequency = frequency1;
        omega = TwoPI / frequency;
        data = new double[cells][cells];
    }

    // this fills in "data" by calling "ray" a bunch of times in different
    // directions
    public void calculate() {
        for (int i = 0; i < rays; i++) {
            //System.out.println(i+"\t"+i/(TwoPI));
            ray(centerX, centerY, i * TwoPI / rays, amplitudeInit, 0, 0);

            if(i%100 == 0){
                //System.out.println("DEBUG "+i/100);
                //draw();

            }
        }
    }

    // This is the hard one
    // it traces one wave across the plate, updating "data" for each cell it
    // touches
    // when it hits a wall it reflects, recursively calling another ray ???
    public void ray(double x, double y, double angle, double amplitude,
            double phase, int depth) {
        rayCount++;
        depth++;
        //System.out.println(depth);
        if (amplitude < THRESHHOLD || depth >= 25) {
            return;
        }

        angle %= TwoPI;
        phase %= TwoPI;

        int collisionCase = 0;

        // check for collisions
        double dist = 0;
        // top bottom
        if (angle < Math.PI) {
            dist = (1 - y) / (Math.sin(angle));
            collisionCase = 0;
        } else {
            dist = (0 - y) / (Math.sin(angle));
            collisionCase = 1;

        }

        debug(dist);

        // left right
        if (angle > Math.PI / 2 && angle < 3 * Math.PI / 2) {
            double temp = (0 - x) / Math.cos(angle);
            if (temp < dist) {
                dist = temp;
                collisionCase = 2;
            }
        } else {
            double temp = (maxX - x) / Math.cos(angle);
            if (temp < dist) {
                dist = temp;
                collisionCase = 3;
            }
        }
        
        
        if(dist < .002){
            return;
        }

        debug(dist);

        for (int i = 1; i < dist * cells; i++) {
            int xcell = (int) ((x + (i * step) * Math.cos(angle)) * cells);
            int ycell = (int) ((y + (i * step) * Math.sin(angle)) * cells);
            
            double currentDist = dist(x, y, xcell, ycell);

            data[ycell][xcell] += Math.pow(distDecay,  currentDist)*amplitude
                    * Math.cos(omega * currentDist + phase);
            debug(data[ycell][xcell]);
            // System.out.println(xcell+"\t"+ycell);

        }

        double newAmplitude = decay *Math.pow(distDecay,  dist)* amplitude;
        double newPhase = dist*omega;
        
        
        //dist * omega
        
        /**
        
        for(int i=0;i<100;i++){
            ray(x + dist * Math.cos(angle), y + dist * Math.sin(angle),
                    TwoPI*i/100.0, newAmplitude, 0, depth);
        }
        **/
        
        
        switch (collisionCase) {
        case 0:
            ray(x + dist * Math.cos(angle), y + dist * Math.sin(angle),
                    TwoPI - angle, newAmplitude, newPhase, depth);
            break;
        case 1:
            ray(x + dist * Math.cos(angle), y + dist * Math.sin(angle),
                    TwoPI - angle, newAmplitude, newPhase, depth);
            break;
        case 2:
            ray(x + dist * Math.cos(angle), y + dist * Math.sin(angle),
                    TwoPI - (Math.PI + angle) % TwoPI, newAmplitude, newPhase, depth);
            break;
        case 3:
            ray(x + dist * Math.cos(angle), y + dist * Math.sin(angle),
                    TwoPI - (Math.PI + angle) % TwoPI, newAmplitude, newPhase, depth);
            break;
        }
        

    }

    private double dist(double x, double y, int xcell, int ycell) {
        double dx = x - xcell * step;
        double dy = y - ycell * step;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private static void debug(Object string) {
        // System.out.println(string);
    }

    // draws the plate based on "data"
    public void draw() {
        StdDraw.show(0);

        for (int y = 0; y < cells; y++) {
            for (int x = 0; x < cells; x++) {
                //int color = (int) ((data[y][x] + 1.5) / 3 * 256);
                //System.out.println(data[y][x]);

                //int color = (int) (Math.abs(data[y][x])*256);
                //Color color = getHue(data[y][x]);
                Color color;
                if(Math.abs(data[y][x]) > MAX_COLOR){
                    color = Color.black;
                }else{
                    color = Color.white;
                }
                debug(color);
                //color = Math.max(color, 0);
                //color = Math.min(color, 255);
                StdDraw.setPenColor(color);
                //StdDraw.filledSquare(x * step, y * step, step);
                StdDraw.pixel(x*step, y*step);
            }
        }

        StdDraw.show(0);

    }

    public static void main(String[] args) {
        StdDraw.setCanvasSize(cells, cells);
        
        
        
        
        int piccount = 0;
        
        for (double freq = 1.2; freq < 20; freq += .02) {
            Medium5 plate1 = new Medium5(1 / freq);
            plate1.calculate();
            plate1.draw();
            
            //StdDraw.save("group4/pic"+piccount+".png");
            System.out.printf("%4d  %3.4f  %6d\n", piccount, 1/freq, plate1.rayCount);
            
            piccount++;
        }

        /**
        for (distDecay = .6; distDecay >0; distDecay-=.02) {
            System.out.println(distDecay);
            Medium3 plate1 = new Medium3(.5);
            // plate1.ray(0, 0, Math.PI/6, 1, 0, 0);
            plate1.calculate();
            plate1.draw();
        }
        **/
        
        
        /**
        Medium3 plate2 = new Medium3(.125);
        plate2.calculate();
        //plate2.ray(.5, .5, .1, 1, 0, 0);
        plate2.draw();
        **/
        
        
        
    }
    
    
    public static Color getHue(double num){
        num = Math.abs(num);
        
        double percent= num / MAX_COLOR;
        percent = Math.min(percent, .5);
         
        if(percent<1.0/6){
            return new Color(255,(int)(255*6*percent),0);
        }else if(percent<2.0/6){
            return new Color((int)(255-255*6*(percent-1.0/6)),255,0);
        }else if(percent<3.0/6){
            return new Color(0,255,(int)(255*6*(percent-2.0/6)));
        }else if(percent<4.0/6){
            return new Color(0,(int)(255-255*6*(percent-3.0/6)),255);
        }else if(percent<5.0/6){
            return new Color((int)(255*6*(percent-4.0/6)),0,255);
        }else if(percent<6.0/6){
            return new Color(255,0,(int)(255-255*6*(percent-5.0/6)));
        }else{
            return Color.blue;
        }
    }

}
