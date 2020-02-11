import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
/*
 * Scanner:
 * 		Tokenizes
 * 		Saves tokens, variables, numbers, and symbol table
 * 		Creates output file
 * 		
 */
public class MyScanner {
    final String REG_REGEX = "((?<=;)|(?=;))|((?<=,)|(?=,))|((?<=!)|(?=!))|((?<=\\+)|(?=\\+))|"+
    "((?<=-)|(?=-))|((?<=\\*)|(?=\\*))|((?<=\\()|(?=\\())|((?<=\\))|(?=\\)))|((?<=\\|)|(?=\\|))";
    final String VARIABLE = "^[a-zA-Z][a-zA-Z0-9]*$";
    final String DATA = "^-?[0-9]*$";
    private int countToken,countID,countDigits,countData;
    private File file1,file2;
    private Scanner fileIn,dataIn;
    private ArrayList<String> scanner,ids,digits,data,variables;
    private HashMap<String, String> hash;
    private HashMap<String, Integer> symbolTable;
    private PrintWriter writer;

    public MyScanner(String programFile,String dataFile)throws FileNotFoundException, UnsupportedEncodingException {
        file1 = new File(programFile);
        file2 = new File(dataFile);
        fileIn = new Scanner(file1);
        dataIn = new Scanner(file2);
        scanner = new ArrayList<String>();
        ids = new ArrayList<String>();
        digits = new ArrayList<String>();
        data = new ArrayList<String>();
        variables = new ArrayList<String>();
        hash = new HashMap<String, String>();
        symbolTable = new HashMap<String, Integer>();
        countToken = 0;
        countID = 0;
        countDigits = 0;
        countData = 0;
        writer = new PrintWriter("ans.out", "UTF-8");
    }
    public void print(String str) {
    	writer.print(str);
    }

    public void close() {
    	writer.close();
    }

    public void read() {
        addHash();
        while (fileIn.hasNext()) {
            String[] temp = fileIn.nextLine().split("\\s+");
            for (int i = 0; i < temp.length; i ++) {
                if (temp[i].length() > 0) {
                    scanner.addAll(splitRegex(temp[i]));
                }
            }
        }

        for (int i = 0; i < scanner.size(); i ++) {
            String check = (String)scanner.get(i);
            if (hash.containsKey(check)) {
                scanner.remove(i);
                scanner.add(i, hash.get(check));
            } else if (check.matches(VARIABLE)) {
                String temp = scanner.get(i);
                scanner.remove(i);
                scanner.addAll(i, MakeID(temp));
                i += temp.length() - 1;
            } else if (check.matches("^[0-9]*$")) {
                String temp = scanner.get(i);
                scanner.remove(i);
                scanner.addAll(i, MakeConst(temp));
                i += temp.length() - 1;
            } else { 
            	System.out.println("Error: Invalid character(s), not available in language");
            	System.exit(0);
            }
        }

        scanner.add("EOF");
        fileIn.close();
    }
    

    public void readData() {

        while (dataIn.hasNext()) {
            String[] temp = dataIn.nextLine().split("\\s+");
            for (int i = 0; i < temp.length; i ++) {
                if (temp[i].length() > 0 && temp[i].matches(DATA)) {
                	data.add(temp[i]);
                }
            }
        }
    }

    public String getToken() {
        return scanner.get(countToken);
    }

    public String getID() {
        return ids.get(countID);
    }

    public String getDigit() {
        return digits.get(countDigits);
    }
    
    public boolean currentToken(String cmpr) {
        return scanner.get(countToken).equals(cmpr);
    }
    public int getInput() {
    	if(countData>data.size()-1) {
    		System.out.println("Error: Not enough input statements for inputs");
    		System.exit(0);
    	}
        return Integer.parseInt(data.get(countData++));
    }
    public void addVaribale(String str) {
    	variables.add(str);
    }
    public boolean containsVaribale(String str) {
    	return variables.contains(str);
    }
    public boolean isAssigned(String str) {
    	return symbolTable.containsKey(str);
    }
    public void replace(String key,int replacing) {
    	symbolTable.replace(key, replacing);
    }

    public void assignVariable(String variable, int input) {
    	symbolTable.put(variable, input);
    }
    public int getOuput(String variable) {
    	if(!symbolTable.containsKey(variable)) {
    		System.out.println("Error: "+variable + " has not been given a value");
    		System.exit(0);    		
    	}
    	return symbolTable.get(variable);
    	
    }
    
    public void nextToken() {
        countToken++;
    }
    
    public void moveID() {
        countID++;
    }

    public void moveDigit() {
        countDigits++;
    }
    private void addHash() {
    	hash.put("!", "!");
    	hash.put("=", "=");
    	hash.put("+", "+");
    	hash.put("-", "-");
    	hash.put("*", "*");		  
    	hash.put("(", "(");
    	hash.put(")", ")");
    	hash.put(":=", ":=");	
    	hash.put("=", "=");	
    	hash.put("<=", "<=");	
    	hash.put("<", "<");	
        hash.put(";", "SEMICOLON");
        hash.put(",", "COMMA");
        hash.put(":=", "ASSIGN");
        hash.put(":", "Colon");
        hash.put("|", "BAR");
        hash.put("program", "PROGRAM");
        hash.put("begin", "BEGIN");
        hash.put("int", "INT");
        hash.put("end", "END");
        hash.put("case", "CASE");
        hash.put("input", "INPUT");
        hash.put("output", "OUTPUT");
        hash.put("if", "IF");
        hash.put("then", "THEN");
        hash.put("else", "ELSE");
        hash.put("endif", "ENDIF");
        hash.put("or", "OR");
        hash.put("while", "WHILE");
        hash.put("begin", "BEGIN");
        hash.put("endwhile", "ENDWHILE");
    }

    private ArrayList<String> splitRegex(String chars) {
        ArrayList<String> tokens = new ArrayList<String>();
        if (chars.contains(":=")) {
            tokens.addAll(breakRegular(chars.split("((?<=:=)|(?=:=))")));
        } else if (chars.contains(":")) {
            tokens.addAll(breakRegular(chars.split("((?<:)|(?=:))")));
        } else if (chars.contains("<=")) {
            tokens.addAll(breakRegular(chars.split("((?<=<=)|(?=<=))")));
        } else if (chars.contains("=")) {
            tokens.addAll(breakRegular(chars.split("((?<==)|(?==))")));
        } else if (chars.contains("<")) {
            tokens.addAll(breakRegular(chars.split("((?<=<)|(?=<))")));
        } else {
            tokens.addAll(Arrays.asList(chars.split(REG_REGEX)));
        }
        return tokens;
    }
    private ArrayList<String> MakeID(String word) {
        ArrayList<String> token = new ArrayList<String>();
        for (int i = 0; i < word.length(); i ++) {
            String temp = String.valueOf(word.charAt(i));
            if (temp.matches("[a-zA-Z]")) {
                ids.add(temp);
                token.add("LETTER");
            } else {
                digits.add(temp);
                token.add("DIGIT");
            }
        }
        return token;
    }
    private ArrayList<String> MakeConst(String word) {
        ArrayList<String> token = new ArrayList<String>();
        for (int i = 0; i < word.length(); i ++) {
            String temp = String.valueOf(word.charAt(i));
            digits.add(temp);
            token.add("DIGIT");
        }
        return token;
    }
    private ArrayList<String> breakRegular(String[] mix) {
        ArrayList<String> temp = new ArrayList<String>();
        for (int i = 0; i < mix.length; i ++) {
            temp.addAll(Arrays.asList(mix[i].split(REG_REGEX)));
        }
        return temp;
    }
}
