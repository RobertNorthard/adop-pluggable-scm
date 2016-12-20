
package pluggable.scm;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import pluggable.configuration.EnvVarProperty;


/**
* A properties store-based implementation of the SCMProviderDataStore.
*
* @author Robert Northard <robertnorthard@googlemail.com>
*/
public class PropertiesSCMProviderDataStore implements SCMProviderDataStore {

  private String propertiesFilePath = "";

  public PropertiesSCMProviderDataStore(){
    this.propertiesFilePath = new EnvVarProperty().getPropertiesPath();
  }

  /**
  * Set the properties file path.
  *
  * @param path - path to properties file.
  */
  public void setPropertiesLocation(String path){
    this.propertiesFilePath = path;
  }

  /**
  * Return a string representation, in an array of all the available SCM providers.
  * @return a string representation, in an array of all the available SCM providers.
  */
  public List<String> getAll(){

    final folder = new File(this.propertiesFilePath);
    List<String> providerList = new ArrayList<String>();

    for (final File fileEntry : folder.listFiles()) {
        if (!fileEntry.isDirectory()){
            String title = fileEntry.getName();
            title = title.replace(".properties", "");
            providerList.add(title);
        }
    }

    return providerList;
  }

  /**
  * Return properties object for the specified SCM provider id.
  * @param id - Unique SCM provider implementation id.
  * @return properties object for the specified SCM provider id.
  */
  public Properties get(String id){

    Properties scmProperties = new Properties();
    InputStream input = null;

    try {

        String file = this.propertiesFilePath + id + ".properties";
        input = new FileInputStream(file);

        // Load the specified properties file
        scmProperties.load(input);

    } catch (IOException ex) {
        throw ex;
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException ex) {
            throw ex;
        }
      }
    }
    return scmProperties;
  }
}
