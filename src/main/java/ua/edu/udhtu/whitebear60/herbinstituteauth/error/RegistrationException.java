package ua.edu.udhtu.whitebear60.herbinstituteauth.error;

public class RegistrationException extends Exception{
    public RegistrationException() {
        super();
    }

    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistrationException(String message) {
        super(message);
    }

    public RegistrationException(Throwable cause) {
        super(cause);
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }
}
