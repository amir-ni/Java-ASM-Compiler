package Core.Exceptions;

public class IllegalRegisterNumberException extends Exception {
    private int line;

    public IllegalRegisterNumberException(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}
