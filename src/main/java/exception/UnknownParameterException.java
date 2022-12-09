package exception;

public class UnknownParameterException extends RuntimeException{
    int position;
    String parameter;

    public UnknownParameterException(int position, String parameter) {
        this.position = position;
        this.parameter = parameter;
    }

    public int getPosition() {
        return position;
    }

    public String getParameter() {
        return parameter;
    }
}
