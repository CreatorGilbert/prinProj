import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
//By Gilberto Molina Badillo
//This is the main file executing the scanner and parser
public class MyInterpreter {
    public static void main(String[] args)throws FileNotFoundException, UnsupportedEncodingException {
    	if(args.length==2) {
    		MyScanner scan = new MyScanner(args[0],args[1]);
        	scan.read();
        	scan.readData();
            Prog pr1 = new Prog(scan);
            pr1.parse();
            pr1.print();
            pr1.execute();
            scan.close();
    	}
    }
}