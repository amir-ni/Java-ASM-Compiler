package Core.Exceptions;

public class SymbolNotFoundException extends Exception {
    private int line;
    private String label;

    public SymbolNotFoundException(int line, String label) {
        this.line = line;
        this.label = label;

    }

    public int getLine() {
        return line;
    }

    public String getLabel() {
        return label;
    }
}
