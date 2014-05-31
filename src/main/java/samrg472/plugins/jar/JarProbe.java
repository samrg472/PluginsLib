package samrg472.plugins.jar;

import samrg472.plugins.exception.FileNotFoundException;
import samrg472.plugins.exception.ManifestNotFoundException;
import samrg472.plugins.exception.NullArgumentException;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @author samrg472
 */
public class JarProbe {

    public final String path;
    public final File file;

    private final JarFile jar;

    /**
     * @param jar file to examine, must be in an archive format of jar or zip
     * @throws FileNotFoundException jar cannot be found
     * @throws IOException I/O error has occurred
     */
    public JarProbe(File jar) throws IOException {
        if (jar == null)
            throw new NullArgumentException("jar");
        if (!jar.isFile())
            throw new FileNotFoundException(jar);
        this.file = jar;
        this.jar = new JarFile(jar);
        this.path = jar.getAbsolutePath();
    }

    /**
     * Gets the specified attribute from the manifest.
     * This can be used to extract information, like an attribute about which class to load in the jar
     * @param attribute attribute to be retrieved from the manifest
     * @return attribute or <code>null</code> if attribute is not found
     * @throws ManifestNotFoundException if the jar doesn't have a manifest
     */
    public String getManifestAttribute(String attribute) throws ManifestNotFoundException {
        if (attribute == null)
            throw new NullArgumentException("attribute");
        try {
            Manifest m = getManifest();
            if (m == null)
                throw new ManifestNotFoundException(new File(path));
            return m.getMainAttributes().getValue(attribute);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Gets the manifest from the jar
     * @return manifest or <code>null</code> if none is found
     */
    public Manifest getManifest() throws IOException {
        return jar.getManifest();
    }

}
