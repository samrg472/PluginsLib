package samrg472.plugins.exception;

/**
 * @author samrg472
 */
public class NullArgumentException extends IllegalArgumentException {

    public NullArgumentException(String param) {
        super(String.format("Parameter \"%s\" cannot be null", param));
    }

}
