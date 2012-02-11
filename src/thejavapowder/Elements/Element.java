package thejavapowder.Elements;
   /* */
public class Element {    /*
	 * Java Powder element class
	 * - Creates elements.
	 *
	 *
	 *
	 */
    public final boolean conductive;
    public final byte weight, burn; //Burn is Around 0-16, Weight is around 0-100 ( More is possible for Both)
    public char state;
    public final String name, description;// In favor of speed
    public final int colour;
    public final byte[] reactives;
    public final byte[][] react = new byte[30][];
    public final byte[][][] heatChanges = new byte[30][][];
	public final byte defaultLife;
    /*
    First depth : Element it would change in
    Second depth : Key Temperature to change
    Third depth : 1 or 2, Must be under (1) or over (2) the Key Temp to change
     */
    public final int defaultTemp;
    //public byte[] transmitTo;

    public Element(
            byte bu,
            byte we,
            boolean co,
            String name,
            String desc,
            int col,
            byte[] react,
            int dTemp,
            byte dLife)
    {
            this.conductive = co;
            this.weight = we;
            this.name = name;
            this.description = desc;
            this.burn = bu;
            this.colour = col;
            this.reactives = react;
            this.defaultTemp = dTemp;
	        this.defaultLife = dLife;
    }

    //public Element(byte[] trans) {
    //    this.transmitTo = trans;
    //}


}