public class Viewer {
    public static void main(String[] args) {

        //StdDraw.picture(.5, .5, "froup1/pic30");

        for (int i = 0; i < 99; i++) {
            System.out.println(i);
            if (i != 36) {
                StdDraw.picture(.5, .5, "group7/pic" + i + ".png");
                StdDraw.show(66);
            }
        }
    }
}
