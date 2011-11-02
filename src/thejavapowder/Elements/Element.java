package thejavapowder.Elements;

public class Element {    /*
	 * Java Powder element class
	 * - Creates elements.
	 *
	 *
	 *
	 */
    public boolean conductive;
    public byte weight, burn; //Burn is Around 0-16, Weight is around 0-100 ( More is possible for Both)
    public char state;
    public String name, description;// In favor of speed
    public int colour;
    public byte[] reactives;
    public final byte[][] react = new byte[16][];
    public int defaultTemp;
    public byte[] transmitTo;

    //Order of creation: new Element(amount-of-burn,weight,conductive(boolean),state("g","p","s","l"), color(HEX), reactives, default temp)
    public Element(byte bu,
                   byte we,
                   boolean co,
                   char st,
                   String name,
                   String desc,
                   int col,
                   byte[] react,
                   int dTemp) {
        this.conductive = co;
        this.weight = we; //will be done later
        this.state = st;
        this.name = name;
        this.description = desc;
        this.burn = bu;
        this.colour = col;
        this.reactives = react;
        this.defaultTemp = dTemp;
    }

    public Element(byte[] trans) {
        this.transmitTo = trans;
    }
    /*
        switch state
        {
            case "g": doGasLoop(); break;
            case "l": doLiqLoop(); break;
            case "p": doPowLoop(); break;
            case "s": doSolLoop(); break;
            default: Console.printtxt("State missing! Aborting"); break;
        }
    */


}