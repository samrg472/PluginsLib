package samrg472.plugins.plugin;

import samrg472.plugins.exception.FileNotFoundException;
import samrg472.plugins.jar.loader.JarLoader;

import java.io.File;
import java.io.IOException;

/**
 * {@link samrg472.plugins.plugin.IPlugin#onLoad()} and {@link samrg472.plugins.plugin.IPlugin#onUnload()}
 * are called as appropriately
 * @author samrg472
 */
public class DefaultPluginManager<T extends IPlugin> extends PluginManager<T> {

    /**
     * Instantiates and calls {@link samrg472.plugins.plugin.IPlugin#onLoad()}
     * @see PluginManager#addPlugin(String, java.io.File, String, java.io.File...) for more documentation
     */
    @Override
    public T addPlugin(String name, File jar, String clazz, File... dependencies) throws FileNotFoundException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        JarLoader<T> loader = super.addPlugin(name, jar, dependencies);
        T instance = loader.createInstance(clazz);
        instance.onLoad();
        return instance;
    }

    /**
     * Calls {@link samrg472.plugins.plugin.IPlugin#onUnload()} and then destroys the plugin
     * @param name plugin name
     * @throws java.io.IOException
     */
    @Override
    public void removePlugin(String name) throws IOException {
        JarLoader<T> loader = plugins.get(name);
        if (loader != null)
            callUnloader(loader);
        super.removePlugin(name);
    }

    /**
     * @see samrg472.plugins.plugin.PluginManager#destroyPlugins()
     */
    @Override
    public void destroyPlugins() throws IOException {
        for (JarLoader<T> loader : plugins.values())
            callUnloader(loader);
        super.destroyPlugins();
    }

    /**
     * Calls {@link samrg472.plugins.plugin.IPlugin#onLoad()} after the object is instantiated
     * @param plugin plugin name
     * @param clazz fully resolved class path
     * @return instance of <code>T</code>
     * @see samrg472.plugins.jar.JarProbe for examining the manifest to get a pointer of where the plugin class is located
     * @see samrg472.plugins.plugin.PluginManager#createInstance(String, String) for more documentation
     */
    @Override
    public T createInstance(String plugin, String clazz) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        T instance = super.createInstance(plugin, clazz);
        instance.onLoad();
        return instance;
    }

    /**
     * Destroys all instances of <code>T</code> and calls {@link samrg472.plugins.plugin.IPlugin#onUnload()}
     * @see samrg472.plugins.plugin.PluginManager#destroyPlugins()
     */
    @Override
    public void destroyInstances() {
        for (JarLoader<T> loader : plugins.values()) {
            callUnloader(loader);
            loader.destroyInstances();
        }
    }

    private void callUnloader(JarLoader<T> loader) {
        for (T instance : loader.getInstances())
            instance.onUnload();
    }
}
