package samrg472.plugins.plugin;

import samrg472.plugins.exception.FileNotFoundException;
import samrg472.plugins.exception.NullArgumentException;
import samrg472.plugins.exception.PluginNotFoundException;
import samrg472.plugins.jar.loader.JarLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @param <T> plugin type, all plugins are instantiated through this
 * @author samrg472
 */
public class PluginManager<T> {

    protected HashMap<String, JarLoader<T>> plugins = new HashMap<String, JarLoader<T>>();

    /**
     * Loads a plugin in the manager
     * @param name plugin name
     * @param loader plugin loader
     * @return plugin loader
     */
    public JarLoader<T> addPlugin(String name, JarLoader<T> loader) {
        if (name == null) throw new NullArgumentException("name");
        if (loader == null) throw new NullArgumentException("loader");
        plugins.put(name, loader);
        return loader;
    }

    /**
     * Loads a plugin in the manager
     * @param name name of the plugin
     * @param jar primary jar to load
     * @param dependencies jar dependencies
     * @return plugin loader
     * @throws FileNotFoundException
     */
    public JarLoader<T> addPlugin(String name, File jar, File... dependencies) throws FileNotFoundException {
        if (name == null)
            throw new NullArgumentException("name");
        JarLoader<T> loader = new JarLoader<T>();
        loader.load(jar);
        for (File f : dependencies)
            loader.load(f);
        plugins.put(name, loader);
        return loader;
    }

    /**
     * Loads a plugin in the manager and then instantiates the plugin
     * @param name name of the plugin
     * @param jar primary jar to load
     * @param clazz fully resolved class path
     * @param dependencies jar dependencies
     * @return an instance of the loaded plugin
     * @throws FileNotFoundException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public T addPlugin(String name, File jar, String clazz, File... dependencies) throws FileNotFoundException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        JarLoader<T> loader = addPlugin(name, jar, dependencies);
        return loader.createInstance(clazz);
    }

    /**
     * Automatically unloads the plugin and then further destroys it
     * @param name plugin name
     * @throws IOException
     */
    public void removePlugin(String name) throws IOException {
        if (name == null)
            throw new NullArgumentException("name");
        IOException e = null;
        JarLoader<T> loader = plugins.get(name);

        if (loader != null) {
            try {
                loader.destroy(false);
            } catch (IOException _e) {
                e = _e;
            }
        }
        plugins.remove(name);
        System.gc();
        if (e != null)
            throw e;
    }

    /**
     * Removes all plugins
     * @throws IOException I/O error
     */
    public void destroyPlugins() throws IOException {
        IOException e = null;
        for (JarLoader<T> loader : plugins.values()) {
            try {
                loader.destroy(false);
            } catch (IOException ex) {
                e = ex;
            }
        }
        plugins.clear();
        System.gc();
        if (e != null)
            throw e;
    }

    /**
     * @param plugin plugin name
     * @param clazz fully resolved class path
     * @return instance of <code>T</code>
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @see samrg472.plugins.jar.JarProbe for examining the manifest to get a pointer of where the plugin class is located
     */
    public T createInstance(String plugin, String clazz) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (plugin == null) throw new NullArgumentException("plugin");
        if (clazz == null) throw new NullArgumentException("clazz");
        if (!plugins.containsKey(plugin))
            throw new PluginNotFoundException(plugin);

        JarLoader<T> loader = plugins.get(plugin);
        return loader.createInstance(clazz);
    }

    /**
     * Gets all instances of the plugin
     * @return All plugin instances
     */
    public List<T> getInstances() {
        List<T> instances = new ArrayList<T>(plugins.size());
        for (JarLoader<T> loader : plugins.values()) {
            instances.addAll(loader.getInstances());
        }
        return Collections.unmodifiableList(instances);
    }

    /**
     * Destroys all instances of <code>T</code>
     */
    public void destroyInstances() {
        for (JarLoader<T> loader : plugins.values())
            loader.destroyInstances();
    }
}
