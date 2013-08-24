package samrg472.plugins.exception;

import java.io.File;
import java.util.jar.JarFile;

/**
 * @author samrg472
 */
public class ManifestNotFoundException extends Exception {

    public ManifestNotFoundException(File file) {
        super(file.getAbsolutePath());
    }

}
