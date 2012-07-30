package thejavapowder.Elements;

public class Energy extends Element{

    public Energy(byte bu, byte we, boolean co, String name, String desc, int col, byte[] react, int dTemp, byte dLife) {
        super(bu, we, co, name, desc, col, react, dTemp, dLife);
        this.state = 'e';
    }

    //public Energy(byte[] trans) {
    //    super(trans);
    //}
}
