package pluggable.scm.helpers;

import java.util.Properties;

/**
* A helper class which contain helpful methods.
*/
public class PropertyUtils {

  /**
  * Get Properties from a file.
  *
  *  @param String file path.
  *  @return Properties object made from file.
  *  @throw FileNotFoundException
  *            If file doesn't exist.
  */
  public static Properties getFileProperties(String filePath){

    if(filePath == null || filePath.equals("")){
      throw new IllegalArgumentException(
        "File path cannot be null or an empty string.")
    }

    File tempFile = new File(filePath);
    Properties fileProperties = new Properties();

    if (!tempFile.exists()) {
      throw new FileNotFoundException("File does not exist: " + filePath);
    }

    InputStream tempInput = new FileInputStream(tempFile);

    fileProperties.load(tempInput);

    return fileProperties;
  }
}
