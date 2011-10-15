package thejavapowder;

public class Console {

    public void printtxt(String strout) {
        TheJavaPowder.consolearea.insert(strout + "\n", 0);
        // consolearea.append(strout + "\n"); // Old function
        // consolearea.setCaretPosition(consolearea.getDocument().getLength());
    }

}
