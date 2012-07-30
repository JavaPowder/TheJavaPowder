package thejavapowder.Elements;


public class Powder extends Element {
    public Powder(byte bu, byte we, boolean co, String name, String desc, int col, byte[] react, int dTemp, byte dLife) {
        super(bu, we, co, name, desc, col, react, dTemp, dLife);
        this.state = 'p';
    }
	public void move() {
		
		
		
	}
    //public Powder(byte[] trans) {
    //    super(trans);
    //}
}
