package javax0.license3j.io;

import javax0.license3j.crypto.LicenseKeyPair;

import java.io.*;
import java.lang.reflect.Modifier;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Class to read a key from file. This class can be used in the application to load the public key and it is also
 * used in the repl application.
 *
 * Create an instance of this class using one of the constructors specifying the source of the license key and then
 * use one of the {@code read...()} methods to read the key into a {@link LicenseKeyPair} object.
 */
public class KeyPairReader implements Closeable {
    private final InputStream is;

    public KeyPairReader(InputStream is) {
        this.is = is;
    }

    public KeyPairReader(File file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }

    public KeyPairReader(String fileName) throws FileNotFoundException {
        this(new File(fileName));
    }

    public LicenseKeyPair readPublic() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        return readPublic(IOFormat.BINARY);
    }

    public LicenseKeyPair readPublic(IOFormat format) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        return read(format, Modifier.PUBLIC);
    }

    public LicenseKeyPair readPrivate() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        return readPrivate(IOFormat.BINARY);
    }

    public LicenseKeyPair readPrivate(IOFormat format) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        return read(format, Modifier.PRIVATE);
    }

    private LicenseKeyPair read(IOFormat format, int type) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        switch (format) {
            case BINARY:
                return LicenseKeyPair.Create.from(ByteArrayReader.readInput(is), type);
            case BASE64:
                return LicenseKeyPair.Create.from(Base64.getDecoder().decode(ByteArrayReader.readInput(is)), type);
        }
        throw new IllegalArgumentException("License format " + format + " is unknown.");
    }

    @Override
    public void close() throws IOException {
        if (is != null) {
            is.close();
        }
    }
}
