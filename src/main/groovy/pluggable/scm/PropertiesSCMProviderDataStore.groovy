
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

  /**
  * Return a string representation, in an array of all the available SCM providers.
  * @return a string representation, in an array of all the available SCM providers.
  */
  public List<String> getAll(){

    String PropertiesPath = EnvVarProperty.getPropertiesLocation()
    final folder = new File(PropertiesPath);
    List<String> ProviderList = new ArrayList<String>();

    for (final File fileEntry : folder.listFiles()) {
        if (fileEntry.isDirectory()) {
            continue;
        } else {
            String title = fileEntry.getName();
            title = title.replace(".properties", "");
            ProviderList.add(title);
        }
    }

    return ProviderList;
  }

  /**
  * Return properties object for the specified SCM provider id.
  * @param id - Unique SCM provider implementation id.
  * @return properties object for the specified SCM provider id.
  */
  public Properties get(String id){

    String PropertiesPath = EnvVarProperty.getPropertiesLocation()

    Properties ScmProperties = new Properties();
    InputStream input = null;

    try {

        private String file = PropertiesPath + id + ".properties";
        input = new FileInputStream(file);

        // Load the specified properties file
        ScmProperties.load(input);

    } catch (IOException ex) {
        ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
      }
    }

    return ScmProperties;

  }

}
