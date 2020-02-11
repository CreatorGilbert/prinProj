/*
 * parser classes that allow the language to be parsed
 * they have:
 * 		a parse method that goes through and makes sure it is in the right format
 * 		a print method that allows it to print as it goes
 * 		a executor method that gets output for the output token
 */
class Prog {
    private DeclSeq dec_seq;
    private StmtSeq stmt_seq;
    private MyScanner scanner;
    private int indent;

    public Prog(MyScanner scan){
        dec_seq = null;
        stmt_seq = null;
        scanner = scan;
        indent = 0;
    }
    public void parse(){
        if (scanner.currentToken("PROGRAM")){
        	scanner.nextToken();
            dec_seq = new DeclSeq(scanner);
            dec_seq.parse(); 
            
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected PROGRAM token in prog");
        	System.exit(0);
        }
        if (scanner.currentToken("BEGIN")){
        	scanner.nextToken();
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected BEGIN toke in progn");
        	System.exit(0);
        }
        if (scanner.currentToken("LETTER") ||scanner.currentToken("DIGIT") || scanner.currentToken("IF") || 
        		scanner.currentToken("WHILE") || scanner.currentToken("INPUT") || 
        		scanner.currentToken("OUTPUT") || scanner.currentToken("CASE")){
        	stmt_seq = new StmtSeq(scanner);
        	stmt_seq.parse(); 
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, IF, WHILE, INPUT, OUTPUT, CASE token in prog");
        	System.exit(0);
        }
        if (scanner.currentToken("END")){
        	scanner.nextToken();
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected END token in prog");
        	System.exit(0);
        }
        if (!scanner.currentToken("EOF")){
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected EOF token in prog");
        	System.exit(0);
        }
    }
    
    public void print() {
    	scanner.print("program\n");
    	indent+=2;
    	dec_seq.print(indent);
    	indent-=2;
    	scanner.print("begin\n");
    	indent+=2;
    	stmt_seq.print(indent);
    	scanner.print("end");
    }    
    public void execute() {
    	dec_seq.execute();
    	stmt_seq.execute();
    }
    
    
}

class DeclSeq {
    private Decl decl;
    private DeclSeq dec_seq;
    private MyScanner scanner;
    private int check;

    public DeclSeq(MyScanner scan){
        decl = null;
        dec_seq = null;
        scanner = scan;
        check = 0;
    }
    public void parse(){
        if (scanner.currentToken("INT")){
        	scanner.nextToken();
            decl = new Decl(scanner);
            decl.parse(); 
            if (scanner.currentToken("INT")){
            	check = 1;
                dec_seq = new DeclSeq(scanner);
                dec_seq.parse(); 
            }
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected INT token in DeclSeq");
        	System.exit(0);
        }
    }
    
    public void print(int indent) {
    	decl.print(indent);
    	if(check==1) {
        	dec_seq.print(indent);
    	}
    }
    public void execute() {
    	decl.execute();
    	if(check==1) {
        	dec_seq.execute();
    	}
    }
}
class StmtSeq {
    private Stmt stmt;
    private StmtSeq stmt_seq;
    private MyScanner scanner;
    private int check;

    public StmtSeq(MyScanner scan){
        stmt = null;
        stmt_seq = null;
        scanner = scan;
        check = 0;
        
    }
    public void parse(){
        if (scanner.currentToken("LETTER") ||scanner.currentToken("DIGIT") || scanner.currentToken("IF") || 
        		scanner.currentToken("WHILE") || scanner.currentToken("INPUT") || 
        		scanner.currentToken("OUTPUT") || scanner.currentToken("CASE")){
        	stmt = new Stmt(scanner);
            stmt.parse(); 
            if(scanner.currentToken("LETTER") ||scanner.currentToken("DIGIT") || scanner.currentToken("IF") || 
            		scanner.currentToken("WHILE") || scanner.currentToken("INPUT") || 
            		scanner.currentToken("OUTPUT") || scanner.currentToken("CASE")){
            	check = 1;
                stmt_seq = new StmtSeq(scanner);
                stmt_seq.parse(); 
            }
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, IF, WHILE, INPUT, OUTPUT, or CASE token in StmtSeq");
        	System.exit(0);
        }
    }

    public void print(int indent) {
    	stmt.print(indent);
    	if(check == 1) {
    		stmt_seq.print(indent);
    	}
    }
    public void execute() {
    	stmt.execute();
    	if(check == 1) {
    		stmt_seq.execute();
    	}
    }
}

class Decl {
    private IdList id_list;
    private MyScanner scanner;

    public Decl(MyScanner scan){
        id_list = null;
        scanner = scan;
    }
    public void parse(){
        if (scanner.currentToken("LETTER")){
            id_list = new IdList(scanner);
            id_list.parse(); 
        }
        if(scanner.currentToken("SEMICOLON")) {
        	scanner.nextToken();
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected SEMICOLON token in Decl");
        	System.exit(0);
        }
    }
    public void print(int indent) {
		String spaces = "";
		for(int i = 0; i<indent; i++) {
			spaces+=" ";
		}
    	scanner.print(spaces + "int ");
    	id_list.print(indent);
    	scanner.print(";\n");
    }
    public void execute() {
    	String idWithComma = id_list.execute();
    	String[] ids = idWithComma.split(",");
    	for(int i=0; i<ids.length; i++) {
    		scanner.addVaribale(ids[i]);
    	}
    }
}

class IdList {
    private IdList id_list;
    private ID id;
    private MyScanner scanner;
    private int check;

    public IdList(MyScanner scan){
        id_list = null;
        id = null;
        scanner = scan;
        check = 0;
    }
    public void parse(){
        if (scanner.currentToken("LETTER")){
            id = new ID(scanner);
            id.parse(); 
        }
        if(scanner.currentToken("COMMA")) {
        	check = 1;
        	scanner.nextToken();
        	if(scanner.currentToken("LETTER")) {
                id_list = new IdList(scanner);
                id_list.parse(); 
        	}else {
            	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, IF, WHILE, INPUT, OUTPUT, or CASE token in IdList");
            	System.exit(0);
        	}
        }
    }
    public void print(int indent) {
    	
    	id.print(indent);
    	if(check == 1) {
    		scanner.print(",");
    		id_list.print(indent);
    	}
    }
    public String execute() {
    	String ans="";
    	ans+=id.execute();
    	if(check == 1) {
    		ans+=","+id_list.execute();
    	}
    	return ans;
    }
}

class Stmt {
    private Assign assign;
    private If _if_;
    private Loop loop;
    private In input;
    private Out out;
    private Case _case_;
    private MyScanner scanner;
    private int check;

    public Stmt(MyScanner scan){
        assign = null;
        _if_ = null;
        loop = null;
        input = null;
        out = null;
        _case_ = null;
        scanner = scan;
        check = 0;
    }

    public void parse(){
        if (scanner.currentToken("LETTER") ||scanner.currentToken("DIGIT")){
        	check=1;
            assign = new Assign(scanner);
            assign.parse(); 
        } else if (scanner.currentToken("IF")){
        	check=2;
        	scanner.nextToken();
            _if_ = new If(scanner);
            _if_.parse(); 
        } else if (scanner.currentToken("WHILE")){
        	check=3;
        	scanner.nextToken();
            loop = new Loop(scanner);
            loop.parse(); 
        } else if (scanner.currentToken("INPUT")){
        	check=4;
        	scanner.nextToken();
            input = new In(scanner);
            input.parse(); 
        } else if (scanner.currentToken("OUTPUT")){
        	check=5;
        	scanner.nextToken();
            out = new Out(scanner);
            out.parse(); 
        } else if (scanner.currentToken("CASE")){
        	check=6;
        	scanner.nextToken();
            _case_ = new Case(scanner);
            _case_.parse(); 
        } else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, IF, WHILE, INPUT, OUTPUT, or CASE token in Stmt");
        	System.exit(0);
        }
    }
    public void print(int indent) {
    	switch(check) {
		case 1:
			assign.print(indent);
			break;
		case 2:
			_if_.print(indent);
			break;
		case 3:
			loop.print(indent);
			break;
		case 4:
			input.print(indent);
			break;
		case 5:
			out.print(indent);
			break;
		case 6:
			_case_.print(indent);
			break;
		default:
			break;
		}
    }
    public void execute() {
    	switch(check) {
		case 1:
			assign.execute();
			break;
		case 2:
			_if_.execute();
			break;
		case 3:
			loop.execute();
			break;
		case 4:
			input.execute();
			break;
		case 5:
			out.execute();
			break;
		case 6:
			_case_.execute();
			break;
		default:
			break;
		}
    }
}

class Assign {
    private Expr expr;
    private MyScanner scanner;
    private ID id;

    public Assign(MyScanner scan){
        expr = null;
        id = null;
        scanner = scan;
    }
    public void parse(){
    	if(scanner.currentToken("LETTER")) {
            id = new ID(scanner);
            id.parse();     		
    	}
        if (scanner.currentToken("ASSIGN")){
        	scanner.nextToken();
        	if(scanner.currentToken("DIGIT")||scanner.currentToken("LETTER")) {
                expr = new Expr(scanner);
                expr.parse();         		
        	}else if(scanner.currentToken("(")) {
        		scanner.nextToken();
                expr = new Expr(scanner);
                expr.parse();   
                if(scanner.currentToken(")")) {
                	scanner.nextToken();
                }else {
                	System.out.println("Error: Token was "+scanner.getToken()+"  expected ) token in Assign");
                	System.exit(0);
                }
        	}else {
            	System.out.println("Error: Token was "+scanner.getToken()+"  expected DIGIT, LETTER, or ( token in Assign");
            	System.exit(0);
        	}
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected ASSIGN token in Assign");
        	System.exit(0);
        }
        if(scanner.currentToken("SEMICOLON")) {
        	scanner.nextToken();
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected SEMICOLON token in Assign");
        	System.exit(0);
        }
    }
    public void print(int indent) {
		String spaces = "";
		for(int i = 0; i<indent; i++) {
			spaces+=" ";
		}
		scanner.print(spaces);
    	id.print(indent);
    	scanner.print(":=");
    	expr.print(indent);
    	scanner.print(";\n");
    }
    public void execute() {
    	String temp = id.execute();
    	if(scanner.containsVaribale(temp)) {
    		scanner.assignVariable(temp, expr.execute());
    	}else {
        	System.out.println("Error: varibale "+temp+" was not initialized");
        	System.exit(0);
    	}
    }
}

class In {
    private IdList id_list;
    private MyScanner scanner;

    public In(MyScanner scan){
        id_list = null;
        scanner = scan;
    }
    public void parse(){
        if (scanner.currentToken("LETTER") ||scanner.currentToken("DIGIT")){
            id_list = new IdList(scanner);
            id_list.parse(); 
        }
        if(scanner.currentToken("SEMICOLON")) {
        	scanner.nextToken();
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected SEMICOLON token in In");
        	System.exit(0);
        }
    }
    public void print(int indent) {
		String spaces = "";
		for(int i = 0; i<indent; i++) {
			spaces+=" ";
		}
		
    	scanner.print(spaces+"input ");
    	id_list.print(indent);
    	scanner.print(";\n");
    }
    public void execute() {
    	String idWithComma = id_list.execute();
    	String[] ids = idWithComma.split(",");
    	for(int i=0; i<ids.length; i++) {
    		if(scanner.isAssigned(ids[i])) {
    			scanner.replace(ids[i], scanner.getInput());
    		}else {
    			scanner.assignVariable(ids[i], scanner.getInput());
    		}
    	}
    }
}

class Out {
    private Expr expr;
    private MyScanner scanner;

    public Out(MyScanner scan){
        expr = null;
        scanner = scan;
    }
    
    public void parse(){
    	if(scanner.currentToken("DIGIT")||scanner.currentToken("LETTER")) {
            expr = new Expr(scanner);
            expr.parse();         		
    	}else if(scanner.currentToken("(")) {
    		scanner.nextToken();
            expr = new Expr(scanner);
            expr.parse();   
            if(scanner.currentToken(")")) {
            	scanner.nextToken();
            }else {
            	System.out.println("Error: Token was "+scanner.getToken()+"  expected ) token in Out");
            	System.exit(0);
            }
    	}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected DIGIT, LETTER, ( token in Out");
        	System.exit(0);
    	}
        if(scanner.currentToken("SEMICOLON")) {
        	scanner.nextToken();
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected SEMICOLON token in Out");
        	System.exit(0);
        }
    }
    public void print(int indent) {
		String spaces = "";
		for(int i = 0; i<indent; i++) {
			spaces+=" ";
		}
    	scanner.print(spaces+"output ");
    	expr.print(indent);
    	scanner.print(";\n");
    }
    public void execute() {
    	scanner.print("\n"+expr.execute());
    }
}

class Case {
    private ID id;
    private CaseLine cline;
    private Expr expr;
    private MyScanner scanner;

    public Case(MyScanner scan){
        cline = null;
        expr = null;
        id = null;
        scanner = scan;
    }
    public void parse(){
        if (scanner.currentToken("LETTER")){
            id = new ID(scanner);
            id.parse(); 
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER token in Case");
        	System.exit(0);
        }
        if (scanner.currentToken("OF")){
        	scanner.nextToken();
            cline = new CaseLine(scanner);
            cline.parse(); 
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected OF token in Case");
        	System.exit(0);
        }
        if (scanner.currentToken("ELSE")){
        	scanner.nextToken();
            expr = new Expr(scanner);
            expr.parse(); 
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected ELSE token in Case");
        	System.exit(0);
        }
        if (scanner.currentToken("END")){
        	scanner.nextToken();
        	if(scanner.currentToken("SEMICOLON")) {
        		scanner.nextToken();
        	}else {
            	System.out.println("Error: Token was "+scanner.getToken()+"  expected SEMICOLON token in Case");
            	System.exit(0);
        	}
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected END token in Case");
        	System.exit(0);
        }
    }
    public void print(int indent) {
		String spaces = "";
		for(int i = 0; i<indent; i++) {
			spaces+=" ";
		}
    	scanner.print(spaces+"case ");
    	id.print(indent);
    	scanner.print("of\n");
    	indent+=2;
    	cline.print(indent);
    	scanner.print("else ");
    	expr.print(indent);
    	indent-=2;
    	scanner.print(spaces+"end;\n");
    }
    public void execute() {
    	//make a case statement
    	id.execute();
    	cline.execute();
    	expr.execute();
    }
}

class CaseLine {
    private ConstList cList;
    private Const cnst;
    private Expr expr;
    private CaseLineFollow clf;
    private MyScanner scanner;
    int check;

    public CaseLine(MyScanner scan){
        cList = null;
        expr = null;
        clf = null;
        cnst=null;
        scanner = scan;
        check = 0;
    }

    public void parse(){
        if (scanner.currentToken("DIGIT")){
        	cnst = new Const(scanner);
        	cnst.parse(); 
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected DIGIT token in CaseLine");
        	System.exit(0);
        }
        if (scanner.currentToken("COMMA")){
        	scanner.nextToken();
            cList = new ConstList(scanner);
            cList.parse(); 
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected COMMA token in CaseLine");
        	System.exit(0);
        }
        if (scanner.currentToken("COLON")){
        	scanner.nextToken();
        	expr = new Expr(scanner);
        	expr.parse(); 
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected COLON token in CaseLine");
        	System.exit(0);
        }
        if (scanner.currentToken("BAR")){
        	check = 1;
        	scanner.nextToken();
            clf = new CaseLineFollow(scanner);
            clf.parse(); 
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected BAR token in CaseLine");
        	System.exit(0);
        }
    }
    public void print(int indent) {
		String spaces = "";
		for(int i = 0; i<indent; i++) {
			spaces+=" ";
		}
		scanner.print(spaces);
    	cnst.print(indent);
    	cList.print(indent);
    	scanner.print(":");
    	expr.print(indent);
		scanner.print("\n"+spaces);
    	if(check==1) {
        	clf.print(indent);
    	}
    }
    public void execute() {
    	//return a case
    	expr.execute();
    	if(check==1) {
        	clf.execute();
    	}
    }
}

class ConstList {
	private Const cnst;
    private ConstList cList;
    private MyScanner scanner;

    public ConstList(MyScanner scan){
    	cnst = null;
        cList = null;
        scanner = scan;
    }

    public void parse(){
        if (scanner.currentToken("DIGIT")){
        	cnst = new Const(scanner);
        	cnst.parse(); 
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected DIGT token in ConstList");
        	System.exit(0);
        }
        if (scanner.currentToken("COMMA")){
        	scanner.nextToken();
            cList = new ConstList(scanner);
            cList.parse(); 
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected COMMA token in ConstList");
        	System.exit(0);
        }
    }
    public void print(int indent) {
    	scanner.print(",");
    	cnst.print(indent);
    	cList.print(indent);
    }
    public void execute() {
    	//adding them but not implemetning them
    	cnst.execute();
    	cList.execute();
    }
}

class CaseLineFollow {
    private CaseLine cline;
    private MyScanner scanner;


    public CaseLineFollow(MyScanner scan){
        cline = null;
        scanner = scan;
    }

    public void parse(){
        if (scanner.currentToken("DIGIT")){
            cline = new CaseLine(scanner);
            cline.parse(); 
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected DIGIT token in ConstList");
        	System.exit(0);
        }
    }
    public void print(int indent) {
		String spaces = "";
		for(int i = 0; i<indent; i++) {
			spaces+=" ";
		}
    	scanner.print(spaces+"|");
    	cline.print(indent);
    }
    public void execute() {
    	//add another case
    	cline.execute();
    }
}

class If {
    private Cond cond;
    private StmtSeq sSeq1, sSeq2;
    private MyScanner scanner;
    private int check;

    public If(MyScanner scan){
        cond = null;
        sSeq1 = null;
        sSeq2 = null;
        scanner = scan;
        check=0;
    }

    public void parse() {
		if(scanner.currentToken("DIGIT")||scanner.currentToken("LETTER")||
				scanner.currentToken("(")||scanner.currentToken("!")) {
			cond = new Cond(scanner);
			cond.parse(); 
		}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected DIGIT, LETTER, (, or ! token in If");
        	System.exit(0);
        }
    	if(scanner.currentToken("THEN")) {
    		scanner.nextToken();
    	}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected THEN token in If");
        	System.exit(0);
    	}
        if (scanner.currentToken("LETTER") ||scanner.currentToken("DIGIT") || scanner.currentToken("IF") || 
        		scanner.currentToken("WHILE") || scanner.currentToken("INPUT") || 
        		scanner.currentToken("OUTPUT") || scanner.currentToken("CASE")){
        	sSeq1 = new StmtSeq(scanner);
        	sSeq1.parse(); 
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, IF, WHILE, INPUT, OUTPUT or CASE token in If");
        	System.exit(0);
        }
        if(scanner.currentToken("ENDIF")) {
    		scanner.nextToken();
    		if(scanner.currentToken("SEMICOLON")) {
    			scanner.nextToken();
    		}else {
            	System.out.println("Error: Token was "+scanner.getToken()+"  expected SEMICOLON token in If");
            	System.exit(0);
    		}
    	}else if(scanner.currentToken("ELSE")) {
    		check=1;
    		scanner.nextToken();
    		if (scanner.currentToken("LETTER") ||scanner.currentToken("DIGIT") || scanner.currentToken("IF") || 
            		scanner.currentToken("WHILE") || scanner.currentToken("INPUT") || 
            		scanner.currentToken("OUTPUT") || scanner.currentToken("CASE")){
        		sSeq2 = new StmtSeq(scanner);
        		sSeq2.parse(); 
            }else {
            	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, IF, WHILE, INPUT, OUTPUT or CASE token in If");
            	System.exit(0);
            }
        	if(scanner.currentToken("ENDIF")) {
        		scanner.nextToken();
        		if(scanner.currentToken("SEMICOLON")) {
        			scanner.nextToken();
        		}else {
                	System.out.println("Error: Token was "+scanner.getToken()+"  expected SEMICOLON token in If");
                	System.exit(0);
        		}
        	}else {
            	System.out.println("Error: Token was "+scanner.getToken()+"  expected ENDIF token in If");
            	System.exit(0);
        	}
    	}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected ENDIF or ELSE token in If");
        	System.exit(0);
    	}
    }
    public void print(int indent) {
		String spaces = "";
		for(int i = 0; i<indent; i++) {
			spaces+=" ";
		}
    	if(check==0) {
    		scanner.print(spaces+"if ");
    		cond.print(indent);
    		scanner.print(" then\n  "+spaces);
    		sSeq1.print(indent);
    		scanner.print(spaces+"endif;\n");
    	}else {
    		scanner.print(spaces+"if ");
    		cond.print(indent);
    		scanner.print(" then\n");
    		indent+=2;
    		sSeq1.print(indent);
    		indent-=2;
    		scanner.print(spaces+"else\n");
    		indent+=2;
    		sSeq2.print(indent);
    		indent-=2;
    		scanner.print(spaces+"endif;\n");
    	}
    }
    public void execute() {
    	if(check==0) {
        	if(cond.execute()) {
        		sSeq1.execute();
        	}    		
    	}else {
        	if(cond.execute()) {
        		sSeq1.execute();
        	}else {
        		sSeq2.execute();
        	}
    	}
    }
}

class Loop {
    private Cond cond;
    private StmtSeq sSeq;
    private MyScanner scanner;

    public Loop(MyScanner scan){
        cond = null;
        sSeq = null;
        scanner = scan;
    }

    public void parse(){
		if(scanner.currentToken("DIGIT")||scanner.currentToken("LETTER")||
				scanner.currentToken("(")||scanner.currentToken("!")) {
			cond = new Cond(scanner);
			cond.parse(); 
		}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected DIGIT, LETTER, ( OR ! token in Loop");
        	System.exit(0);
        }
    	
    	if(scanner.currentToken("BEGIN")) {
    		scanner.nextToken();
    	}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected BEGIN token in Loop");
        	System.exit(0);
    	}
    	
    	if (scanner.currentToken("LETTER") ||scanner.currentToken("DIGIT") || scanner.currentToken("IF") || 
        		scanner.currentToken("WHILE") || scanner.currentToken("INPUT") || 
        		scanner.currentToken("OUTPUT") || scanner.currentToken("CASE")){
    		sSeq = new StmtSeq(scanner);
    		sSeq.parse(); 
        }else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, IF, WHILE, INPUT, OUTPUT or CASE token in Loop");
        	System.exit(0);
        }
    	
    	if(scanner.currentToken("ENDWHILE")) {
    		scanner.nextToken();
    	}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected ENDWHILE token in Loop");
        	System.exit(0);
    	}
    	
    	if(scanner.currentToken("SEMICOLON")) {
    		scanner.nextToken();
    	}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected SEMICOLON token in Loop");
        	System.exit(0);
    	}
    }
    
    public void print(int indent) {
		String spaces = "";
		for(int i = 0; i<indent; i++) {
			spaces+=" ";
		}
    	scanner.print(spaces+"while ");
    	cond.print(indent);
    	scanner.print(" begin\n");
    	indent+=2;
    	sSeq.print(indent);
    	indent-=2;
    	scanner.print(spaces+"endwhile;\n");
    }
    public void execute() {
    	while(cond.execute()) {
    		sSeq.execute();
    	}
    }
}

class Cond {
    private Cmpr cmpr;
    private Cond cond;
    private MyScanner scanner;
    private int check;

    public Cond(MyScanner scan){
        cmpr = null;
        cond = null;
        scanner = scan;
        check = 0;
    }

    public void parse(){
    	if(scanner.currentToken("LETTER")||scanner.currentToken("DIGIT")||
    		scanner.currentToken("(")){
    		cmpr = new Cmpr(scanner);
    		cmpr.parse(); 
    	}else if(scanner.currentToken("!")) {
    		check = 1;
    		scanner.nextToken();
    		if(scanner.currentToken("(")) {
    			scanner.nextToken();
    			if(scanner.currentToken("LETTER")||scanner.currentToken("DIGIT")||
    				scanner.currentToken("(")||scanner.currentToken("!")) {
    				cond = new Cond(scanner);
    				cond.parse(); 
    			}else {
                	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, ( or ! token in Cond");
                	System.exit(0);
    			}
    			if(scanner.currentToken(")")){
    				scanner.nextToken();
    			}else {
                	System.out.println("Error: Token was "+scanner.getToken()+"  expected ) token in Cond");
                	System.exit(0);
    			}
    		}else {
            	System.out.println("Error: Token was "+scanner.getToken()+"  expected ( token in Cond");
            	System.exit(0);
    		}
    	}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, OR ! token in Cond");
        	System.exit(0);
    	}
    	if(scanner.currentToken("OR")) {
    		check = 2;
    		scanner.nextToken();
			if(scanner.currentToken("LETTER")||scanner.currentToken("DIGIT")||
				scanner.currentToken("(")||scanner.currentToken("!")) {
				cond = new Cond(scanner);
				cond.parse(); 
			}else {
            	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, ( or ! token in Cond");
            	System.exit(0);
			}
    	}
    }
    public void print(int indent) {
    	if(check==1) {
    		scanner.print("!(");
    		cond.print(indent);
    		scanner.print(")");
    	}else{
    		cmpr.print(indent);
    		if(check==2) {
    			scanner.print("or ");
    			cond.print(indent);
    		}
    		
    	}
    }
    public boolean execute() {
    	boolean ans = false;
    	if(check==1) {
    		ans = !cond.execute();
    	}else {
    		if(check==2) {
    			if(cmpr.execute()||cond.execute())
    				ans = true;
    		}else {
    			ans = cmpr.execute();
    		}
    	}
    	return ans;
    }
}

class Cmpr {
    private Expr expr1, expr2;
    private MyScanner scanner;
    private int check;

    public Cmpr(MyScanner scan){
        expr1 = null;
        expr2 = null;
        scanner = scan;
        check = 0;
    }

    public void parse(){
    	if(scanner.currentToken("DIGIT")||scanner.currentToken("LETTER")||
    		scanner.currentToken("(")) {
    		expr1 = new Expr(scanner);
    		expr1.parse(); 
    	}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, ( token in Cmpr");
        	System.exit(0);
    	}
    	if(scanner.currentToken("<")) {
    		check=1;
    		scanner.nextToken();
    		
    	}else if(scanner.currentToken("=")) {
    		check=2;
    		scanner.nextToken();
    	}else if(scanner.currentToken("<=")){
    		check=3;
    		scanner.nextToken();
    	}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected <, <=, or = token in Cmpr");
        	System.exit(0);
    	}
    	if(scanner.currentToken("DIGIT")||scanner.currentToken("LETTER")||
        		scanner.currentToken("(")) {
        		expr2 = new Expr(scanner);
        		expr2.parse(); 
    	}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, ( token in Cmpr");
        	System.exit(0);
    	}
    }
    public void print(int indent) {
    	expr1.print(indent);
    	if(check==1) {
    		scanner.print("<");
    	}else if(check==2) {
    		scanner.print("=");
    	}else {
    		scanner.print("<=");
    	}
    	expr2.print(indent);
    }
    public boolean execute() {
    	boolean ans = false;
    	if(check==1) {
    		if(expr1.execute()<expr2.execute()) {
    			ans = true;
    		}
    	}else if(check==2) {
    		if(expr1.execute()==expr2.execute()) {
    			ans = true;
    		}
    	}else {
    		if(expr1.execute()<=expr2.execute()) {
    			ans = true;
    		}
    	}
    	return ans;
    }
}

class Expr {
    private Term term;
    private Expr expr;
    private MyScanner scanner;
    private int check;

    public Expr(MyScanner scan){
        term = null;
        expr = null;
        scanner = scan;
        check=0;
    }

    public void parse(){
    	if(scanner.currentToken("DIGIT")||scanner.currentToken("LETTER")||
        		scanner.currentToken("(")) {
        		term = new Term(scanner);
        		term.parse(); 
    	}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, ( token in Expr");
        	System.exit(0);
    	}
    	if(scanner.currentToken("+")||scanner.currentToken("-")) {
    		if(scanner.currentToken("+")) {
    			check=1;
    		}else {
    			check=2;
    		}
    		scanner.nextToken();
        	if(scanner.currentToken("DIGIT")||scanner.currentToken("LETTER")||
            		scanner.currentToken("(")) {
            		expr = new Expr(scanner);
            		expr.parse(); 
        	}else {
            	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, ( token in Expr");
            	System.exit(0);
        	}
    	}
    }
    public void print(int indent) {
    	term.print(indent);
    	if(check==1) {
    		scanner.print("+");
    		expr.print(indent);
    	}else if (check == 2) {
    		scanner.print("-");
    		expr.print(indent);
    	}
    }
    public int execute() {
    	int ans = term.execute();
    	if(check==1) {
    		ans+=expr.execute();
    	}else if (check==2) {
    		ans-=expr.execute();
    	}
    	return ans;
    }
}

class Term {
    private Factor fact;
    private Term term;
    private MyScanner scanner;
    private int check;
    
    public Term(MyScanner scan){
        fact = null;
        term = null;
        scanner = scan;
        check = 0;
    }
    public void parse(){
    	if(scanner.currentToken("DIGIT")||scanner.currentToken("LETTER")||
        		scanner.currentToken("(")) {
        		fact = new Factor(scanner);
        		fact.parse(); 
    	}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, ( token in term");
        	System.exit(0);
    	}
    	if(scanner.currentToken("*")) {
    		check = 1;
    		scanner.nextToken();
    		if(scanner.currentToken("DIGIT")||scanner.currentToken("LETTER")||
            		scanner.currentToken("(")) {
    			term = new Term(scanner);
    			term.parse(); 
    		}else {
            	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, ( token in Term");
            	System.exit(0);
    		}
    	}
    }
    public void print(int indent) {
    	fact.print(indent);
    	if(check==1) {
    		scanner.print("*");
    		term.print(indent);
    	}
    }
    public int execute() {
    	int ans = fact.execute();
    	if(check==1) {
    		ans*=term.execute();
    	}
    	return ans;
    }
}

class Factor {
	private Const cnst;
	private ID id;
    private Expr expr;
    private MyScanner scanner;
    private int check;
    
    public Factor(MyScanner scan){
    	cnst = null;
    	id = null;
        expr = null;
        scanner = scan;
        check = 0;
    }
    public void parse(){
    	if(scanner.currentToken("LETTER")) {
    		check = 1;
    		id = new ID(scanner);
    		id.parse(); 
    	}else if(scanner.currentToken("DIGIT")){
    		check = 2;
    		cnst = new Const(scanner);
    		cnst.parse(); 
    	}else if(scanner.currentToken("(")) {
    		check = 3;
    		scanner.nextToken();
    		expr = new Expr(scanner);
    		expr.parse(); 
    		if(scanner.currentToken(")")) {
    			scanner.nextToken();
    		}else {
            	System.out.println("Error: Token was "+scanner.getToken()+"  expected ) token in Factor");
            	System.exit(0);
    		}
    	}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER, DIGIT, ( token in Factor");
        	System.exit(0);
    	}
    }
    public void print(int indent) {
    	if(check==1) {
    		id.print(indent);
    	}else if(check==2) {
    		cnst.print(indent);
    	}else {
    		scanner.print("(");
    		expr.print(indent);
    		scanner.print(")");
    	}
    }
    public int execute() {
    	int ans;
    	if(check==1) {
    		ans = scanner.getOuput(id.execute());
    	}else if(check==2) {
    		ans = cnst.execute();
    	}else {
    		ans = expr.execute();
    	}
    	return ans;
    }
}

class ID {
    private Letter letter;
    private ID id;
    private Digit digit;
    private MyScanner scanner;
    private int check1, check2;
    
    public ID(MyScanner scan){
        letter = null;
        id = null;
        digit = null;
        scanner = scan;
        check1 = 0;
        check2 = 0;
    }
    public void parse(){
    	if(scanner.currentToken("LETTER")) {
    		check1 = 1;
    		letter = new Letter(scanner);
    		letter.parse(); 
    	}else if(scanner.currentToken("DIGIT")) {
    		check1 = 2;
    		digit = new Digit(scanner);
    		digit.parse(); 
    	}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected LETTER or DIGIT token in ID");
        	System.exit(0);
    	}
    	
    	if(scanner.currentToken("LETTER")||scanner.currentToken("DIGIT")) {
    		check2 = 1;
    		id = new ID(scanner);
    		id.parse(); 
    	}
    }
    public void print(int indent) {
    	if(check1 == 1) {
    		letter.print(indent);
    	}else {
    		digit.print(indent);
    	}
    	if(check2==1) {
    		id.print(indent);
    	}
    }
    public String execute() {
    	String ans ="";
    	if(check1 == 1) {
    		ans+=letter.execute();
    	}else {
    		ans+=digit.execute();
    	}
    	if(check2==1) {
    		ans+=id.execute();
    	}
    	return ans;
    }
}

class Letter {
    private MyScanner scanner;
    private String letter;
    
    public Letter(MyScanner scan){
    	scanner = scan;
    }
    public void parse() {
    	letter = scanner.getID();
    	scanner.moveID();
    	scanner.nextToken();
    }
    public void print(int indent) {
    	scanner.print(letter);
    }
    public String execute() {
    	return letter;
    }
}

class Const {
    private Digit digit;
    private Const const1;
    private MyScanner scanner;
    private int check;
    
    public Const(MyScanner scan){
        digit = null;
        const1 = null;
        scanner = scan;
        check = 0;
    }
    public void parse(){
    	if(scanner.currentToken("DIGIT")) {
    		digit = new Digit(scanner);
    		digit.parse(); 
    	}else {
        	System.out.println("Error: Token was "+scanner.getToken()+"  expected DIGIT token in Const");
        	System.exit(0);
    	}
    	if(scanner.currentToken("DIGIT")) {
    		check = 1;
			const1 = new Const(scanner);
			const1.parse(); 
    	}
    }
    public void print(int indent) {
    	digit.print(indent);
    	if(check == 1) {
    		const1.print(indent);
    	}
    }
    public int execute() {
    	String ans = digit.execute();
    	if(check == 1) {
    		ans += const1.execute();
    	}
    	return Integer.parseInt(ans);
    }
}

class Digit {
    private MyScanner scanner;
    private String digit;
    
    public Digit(MyScanner scan){
    	scanner=scan;
    }
    public void parse() {
    	digit = scanner.getDigit();
    	scanner.moveDigit();
    	scanner.nextToken();
    }
    public void print(int indent) {
    	scanner.print(digit);
    }
    public String execute() {
    	return digit;
    }
}