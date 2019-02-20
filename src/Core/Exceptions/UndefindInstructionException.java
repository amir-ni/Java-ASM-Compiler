package Core.Exceptions;

public class UndefindInstructionException extends Exception {
    private int line;

    public UndefindInstructionException(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
