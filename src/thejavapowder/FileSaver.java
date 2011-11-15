package thejavapowder;


import org.w3c.dom.*;

import java.io.*;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.xml.sax.SAXException;


class FileSaver {
    Variables var = thejavapowder.TheJavaPowder.var;
    Console console = thejavapowder.TheJavaPowder.console;
    public void savePref()
    {


        try {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();


		Document settings = docBuilder.newDocument();
		Element rootElement = settings.createElement("settings");
		settings.appendChild(rootElement);

		Element width = settings.createElement("width");
		width.appendChild(settings.createTextNode("" + var.Width));

		Element height = settings.createElement("height");
		height.appendChild(settings.createTextNode("" + var.Height));

        Element zoom = settings.createElement("zoom");
		zoom.appendChild(settings.createTextNode("" + var.winZoom));

            rootElement.appendChild(width);
            rootElement.appendChild(height);
            rootElement.appendChild(zoom);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(settings);
		StreamResult result = new StreamResult(new File("jpsettings.xml"));

		transformer.transform(source, result);

	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }


    }

    public void loadPref()
    {
      //if(){
        try {
            File fXmlFile = new File("jpsettings.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList set = doc.getElementsByTagName("settings");
		    Node setN = set.item(0);

            Element eElement = (Element) setN;
            Node nNode = set.item(0);
            if (setN.getNodeType() == Node.ELEMENT_NODE) {
             var.Width = Integer.parseInt(getTagValue("width", eElement));
             var.Height = Integer.parseInt(getTagValue("height", eElement));
             var.winZoom = (byte)Integer.parseInt(getTagValue("zoom", eElement));
             }

            } catch (IOException e) {
            } catch (ParserConfigurationException e) {
        } catch (SAXException e) {
        }

      //}
    }

    private static String getTagValue(String sTag, Element eElement) {
	NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

        Node nValue = nlList.item(0);

	return nValue.getNodeValue();
  }

    /*The FileSaver class
      *Methods: boolean saveFile(), boolean readFile()
      *Fields that must be set before methods called:
      *filename - for saveFile
      *readFileName - for readFile
      *sizex - for both
      *sizey - for both
      */

    FileOutputStream outp;
    FileInputStream inp;

    int sizex, sizey;

    String filename, readFileName;
    byte[] databuffer2;
    byte[][] data;

    // File read and save code below
    File file;
    Writer writer;
    Random randomSaveName = new Random(1337);

        public void SaveFile() {
        try {
            console.printtxt("Saving...");
            file = new File("" + randomSaveName.nextInt() + ".jps");
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("javapowder-save\n");
           // writer.write("version:" + var.versionID + "\n");
            writer.write("map:\n");
            for (int y = 0; y < var.Height; y++) {
                for (int x = 0; x < var.Width; x++) {
                    writer.write((int) var.Map[x][y]);
                }
            }
            writer.write("vmap:\n");
            for (int y = 0; y < var.Height; y++) {
                for (int x = 0; x < var.Width; x++) {
                    writer.write((int) var.VMap[x][y]);
                }
            }
            writer.write("pmap:\n");
            for (int y = 0; y < var.Height; y++) {
                for (int x = 0; x < var.Width; x++) {
                    writer.write((int) var.PMap[x][y]);
                }

                console.printtxt("Write ended successfully");
            }
        } catch (IOException exception) {
            console.printtxt("I/O Exception!" + exception.getMessage());
        } finally {
            try {
                writer.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
            console.printtxt("Write operation ended.");
        }


    boolean readFile() {
        if (readFileName.equals("")) readFileName = "default";
        try {
            inp = new FileInputStream(readFileName);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } // Creates the input
        try {
            databuffer2 = new byte[inp.available() - 3];
        } catch (IOException e) {

            e.printStackTrace();
        } // Temporarily holds the file data
        data = new byte[sizex][sizey]; // The finished save data

        try {
            inp.skip(3);
        } catch (IOException e) {

            e.printStackTrace();
        } // Skipping the header

        try {
            for (int i2 = 0; i2 < inp.available() - 3; i2++) // The read from the file to the temp data buffer
                databuffer2[i2] = (byte) inp.read();
        } catch (IOException e) {

            e.printStackTrace();
        } // The read call

        for (int i = 0; i < sizex; i++) // The re-arranger(from 1d to 2d) - code by Bala R from Stack Overflow.
            for (int j = 0; j < sizey; j++)
                data[i][j] = (byte) (databuffer2[(j * sizex) + i] - 127);

        return true;
    }*/
}
		
	
		