package thejavapowder.Elements;

public class Solid extends Element {
    public Solid(byte bu, byte we, boolean co, String name, String desc, int col, byte[] react, int dTemp) {
        super(bu, we, co, name, desc, col, react, dTemp);
        this.state = 's';

    }

    public Solid(byte[] trans) {
        super(trans);
    }
}
