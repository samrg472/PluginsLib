package samrg472.plugins.jar.loader;

import java.net.URL;

/**
 * Wrapper class to URLClassLoader
 * @author samrg472
 */
public class URLClassLoader extends java.net.URLClassLoader {

    public URLClassLoader() {
        super(new URL[0]);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }

}
