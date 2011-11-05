package thejavapowder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

class FileSaver {
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
    byte[] versionH = new byte[]{0x65, 0x66, 0x67};

    boolean saveFile() {

        if (filename.equals("")) filename = "default";


        try {
            outp = new FileOutputStream(filename);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }


        data = new byte[sizex][sizey];

        // Transfers data into databuffer and clears data.

        try {

            outp.write(versionH);
            for (int y1 = 0; y1 < sizey; y1++)
                for (int x1 = 0; x1 < sizex; x1++) {
                    outp.write(data[x1][y1] + 127);
                }

        } catch (IOException e1) {
            System.exit(0);
            try {
                outp.flush();
            } catch (IOException e) {

                e.printStackTrace();
            }
            try {
                outp.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
            return false;
        } finally {
            try {

                outp.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return true;
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
    }
}
		
	
		