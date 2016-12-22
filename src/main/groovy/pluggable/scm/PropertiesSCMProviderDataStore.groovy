
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
*/
public class PropertiesSCMProviderDataStore implements SCMProviderDataStore {

  private String propertiesFilePath = "";

  public PropertiesSCMProviderDataStore(){
    try {
      this.propertiesFilePath = EnvVarProperty.getInstance().getPropertiesPath();
    } catch(IllegalArgumentException ex){
      this.propertiesFilePath = "";
    }
  }

  /**
  * Set the properties file path.
  *
  * @param path - path to properties file.
  * @throws IllegalArgumentException
  *         If path is null or an empty string.
  *         If path is not a valid directory.
  */
  public void setPropertiesLocation(String path){

    if(path == null || path.equals("")){
      throw new IllegalArgumentException("Property datastore path cannot be null or an empty string.");
    }

    File directory = new File(path);

    if(directory.exists() && directory.isDirectory()) {
      this.propertiesFilePath = path;
    }else{
      throw new IllegalArgumentException(path + " is not a valid property datastore directory.");
    }
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
            Properties scmProperties = new Properties();
            InputStream input = null;
            input = new FileInputStream(title);
            scmProperties.load(input);
            String providerID = scmProperties.getProperty("scm.id");
            providerList.add(providerID);
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

    if(id == null || id.equals("")){
        throw new IllegalArgumentException("SCM provider id cannot be empty or null");
    }

    final folder = new File(this.propertiesFilePath);
    Properties scmProperties = new Properties();
    InputStream input = null;
    String fileTitle = null;

    // Iterate through all the files and find the one matching the right ID provided
    for (final File fileEntry : folder.listFiles()) {
        if (!fileEntry.isDirectory()){

            String tempTitle = this.propertiesFilePath + fileEntry.getName();
            Properties tempProperties = new Properties();
            InputStream tempInput = null;
            tempInput = new FileInputStream(tempTitle);
            tempProperties.load(tempInput);
            String providerID = tempProperties.getProperty("scm.id");
            if(providerID.equals(id)) {
              fileTitle = tempTitle;
              break
            }

        }
    }

    try {

        String file = fileTitle;
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
