package pluggable.scm.helpers;

import java.util.Properties;

/**
* A helper class which contain helpful methods.
*/
public class HelperUtils {
  /**
  * Get Properties from a file.
  *
  *  @param String file path.
  *  @return Properties object made from file.
  *  @throw FileNotFoundException
  *            If file doesn't exist.
  */
  public static Properties getFileProperties(String filePath){

        File tempFile = new File(filePath);
        Properties fileProperties = new Properties();

        if (!tempFile.exists()) {
          throw new FileNotFoundException("file doesn't exist");
        }

        InputStream tempInput = new FileInputStream(tempFile);
        fileProperties.load(tempInput);

        return fileProperties;
  }
}
