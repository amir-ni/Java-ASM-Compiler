package Core.Exceptions;

public class UndefinedInstructionException extends Exception {
    private int line;

    public UndefinedInstructionException(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
