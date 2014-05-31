package samrg472.plugins.plugin;

/**
 * Default plugin interface, another interface that extends this can be used.
 * Optionally, an entirely different interface can be provided
 * @author samrg472
 */
public interface IPlugin {

    /**
     * Called when the plugin is loaded
     */
    public void onLoad();

    /**
     * Called when the plugin is unloaded
     */
    public void onUnload();

}
