import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    private HashMap<String,Symbol> classSymbols;//for STATIC, FIELD
    private HashMap<String,Symbol> subroutineSymbols;//for ARG, VAR
    private HashMap<Symbol.KIND,Integer> indices;

    /**
     * creates a new empty symbol table
     * init all indices
     */
    public SymbolTable() {
        classSymbols = new HashMap<String, Symbol>();
        subroutineSymbols = new HashMap<String, Symbol>();

        indices = new HashMap<Symbol.KIND, Integer>();
        indices.put(Symbol.KIND.ARG,0);
        indices.put(Symbol.KIND.FIELD,0);
        indices.put(Symbol.KIND.STATIC,0);
        indices.put(Symbol.KIND.VAR,0);

    }

    /**
     * starts a new subroutine scope
     * resets the subroutine's symbol table
     */
    public void startSubroutine(){
        subroutineSymbols.clear();
        indices.put(Symbol.KIND.VAR,0);
        indices.put(Symbol.KIND.ARG,0);
    }

    /**
     * Defines a new identifier of a given name,type and kind
     * and assigns it a running index, STATIC and FIELD identifiers
     * jave a class scope, while ARG and VAR identifiers have a subroutine scope
     * @param name
     * @param type
     * @param kind
     */
    public void define(String name, String type, Symbol.KIND kind){

        if (kind == Symbol.KIND.ARG || kind == Symbol.KIND.VAR){

            int index = indices.get(kind);
            Symbol symbol = new Symbol(type,kind,index);
            indices.put(kind,index+1);
            subroutineSymbols.put(name,symbol);

        }else if(kind == Symbol.KIND.STATIC || kind == Symbol.KIND.FIELD){

            int index = indices.get(kind);
            Symbol symbol = new Symbol(type,kind,index);
            indices.put(kind,index+1);
            classSymbols.put(name,symbol);

        }

    }

    /**
     * returns the number of variables of the given kind already defined in the current scope
     * @param kind
     * @return
     */
    public int varCount(Symbol.KIND kind){
        return indices.get(kind);
    }

    /**
     * returns the kind of the named identifier in the current scope
     * if the identifier is unknown in the current scope returns NONE
     * @param name
     * @return
     */
    public Symbol.KIND kindOf(String name){

        Symbol symbol = lookUp(name);

        if (symbol != null) return symbol.getKind();

        return Symbol.KIND.NONE;
    }

    /**
     * returns the type of the named identifier in the current scope
     * @param name
     * @return
     */
    public String typeOf(String name){

        Symbol symbol = lookUp(name);

        if (symbol != null) return symbol.getType();

        return "";
    }

    /**
     * returns the index assigned to the named identifier
     * @param name
     * @return
     */
    public int indexOf(String name){

        Symbol symbol = lookUp(name);

        if (symbol != null) return symbol.getIndex();

        return -1;
    }

    /**
     * check if target symbol is exist
     * @param name
     * @return
     */
    private Symbol lookUp(String name){

        if (classSymbols.get(name) != null){
            return classSymbols.get(name);
        }else if (subroutineSymbols.get(name) != null){
            return subroutineSymbols.get(name);
        }else {
            return null;
        }

    }

}
