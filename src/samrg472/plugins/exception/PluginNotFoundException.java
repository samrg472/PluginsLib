package samrg472.plugins.exception;

/**
 * @author samrg472
 */
public class PluginNotFoundException extends RuntimeException {

    public PluginNotFoundException(String pluginName) {
        super("No such plugin: " + pluginName);
    }

}
