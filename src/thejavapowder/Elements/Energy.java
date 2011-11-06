package thejavapowder.Elements;

public class Energy extends Element{

    public Energy(byte bu, byte we, boolean co, String name, String desc, int col, byte[] react, int dTemp) {
        super(bu, we, co, name, desc, col, react, dTemp);
        this.state = 'e';
    }

    public Energy(byte[] trans) {
        super(trans);
    }
}
