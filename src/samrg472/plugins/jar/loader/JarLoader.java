package samrg472.plugins.jar.loader;

import samrg472.plugins.exception.FileNotFoundException;
import samrg472.plugins.exception.NullArgumentException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @param <T> the type plugin instances will be of
 * @author samrg472
 */
public class JarLoader<T> {

    private URLClassLoader loader;

    private List<String> jars = new ArrayList<String>();

    /** Instances of the plugin */
    protected List<T> instances;

    /**
     * Creates a new dynamic jar loader on a new class loader
     */
    public JarLoader() {
        this.loader = new URLClassLoader();
        this.instances = new ArrayList<T>();
    }

    /**
     * Loads the jar into the class loader
     * In most cases the jar with the plugin will only be loaded except for needed dependencies by the jar
     * @param jar jar to be loaded
     * @throws FileNotFoundException jar cannot be located
     */
    public void load(File jar) throws FileNotFoundException {
        if (jar == null)
            throw new NullArgumentException("jar");
        if (!jar.isFile())
            throw new FileNotFoundException(jar);
        try {
            if (jars.contains(jar.getAbsolutePath()))
                return;
            loader.addURL(jar.toURI().toURL());
            jars.add(jar.getAbsolutePath());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e); // Should never happen, but just in case
        }
    }

    /**
     * Remove all instances of the plugin
     */
    public void destroyInstances() {
        instances.clear();
    }

    /**
     * @return instances of the plugin class
     */
    public List<T> getInstances() {
        return Collections.unmodifiableList(instances);
    }

    /**
     * @param clazz fully resolved class name to instantiate, must extend <code>T</code> or an exception will be thrown
     * @return new instance of the plugin
     * @see samrg472.plugins.jar.JarProbe for extracting information about a jar to load
     */
    @SuppressWarnings("unchecked")
    public T createInstance(String clazz) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (clazz == null)
            throw new NullArgumentException("clazz");
        T instance = (T) loader.loadClass(clazz).newInstance();
        instances.add(instance);
        return instance;
    }

    /**
     * Must be called when finished with the instance to properly unload the classes
     * Make sure there are no instance references to any instances here
     * @throws IOException
     */
    public void destroy(boolean gc) throws IOException {
        IOException e = null;
        try {
            loader.close();
        } catch (IOException _e) {
            e = _e;
        }
        instances.clear();
        jars.clear();
        loader = null;
        if (gc)
            System.gc(); // Must be garbage collected to properly unload the classes
        if (e != null)
            throw e;
    }

}
