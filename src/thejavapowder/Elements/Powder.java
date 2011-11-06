package thejavapowder.Elements;


public class Powder extends Element {
    public Powder(byte bu, byte we, boolean co, String name, String desc, int col, byte[] react, int dTemp) {
        super(bu, we, co, name, desc, col, react, dTemp);
        this.state = 'p';
    }

    public Powder(byte[] trans) {
        super(trans);
    }
}
