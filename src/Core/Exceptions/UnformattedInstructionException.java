package Core.Exceptions;

public class UnformattedInstructionException extends Exception {
    private int line;

    public UnformattedInstructionException(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
