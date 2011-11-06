package thejavapowder.Elements;


public class Liquid extends Element {
    public Liquid(byte bu, byte we, boolean co, String name, String desc, int col, byte[] react, int dTemp) {
        super(bu, we, co, name, desc, col, react, dTemp);
        this.state = 'l';
    }

    public Liquid(byte[] trans) {
        super(trans);
    }
}
