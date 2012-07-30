package thejavapowder.Elements;


public class Liquid extends Element {
    public Liquid(byte bu, byte we, boolean co, String name, String desc, int col, byte[] react, int dTemp, byte dLife) {
        super(bu, we, co, name, desc, col, react, dTemp, dLife);
        this.state = 'l';
    }

    //public Liquid(byte[] trans) {
    //    super(trans);
    //}
}
