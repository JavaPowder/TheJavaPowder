package thejavapowder.Elements;

public class Fire extends Element {    /*
	 * Java Powder element class
	 * - Creates elements.
	 * The Switch is not needed since it's in the main loop
	 * (WIP)
	 *
	 *
	 *
	 */

    //Order of creation: new Element(amount-of-burn,weight,conductive(boolean),state("g","p","s","l"), color(HEX))
    public Fire(byte bu,
                  byte we,
                  boolean co,
                  char st,
                  String name,
                  String desc,
                  int col,
                  byte[] react,
                  int dTemp) {
        super(bu,
                we,
                co,
                st,
                name,
                desc,
                col,
                react,
                dTemp);
    }

    public Fire(byte[] trans) {
        super(trans);
    }
}