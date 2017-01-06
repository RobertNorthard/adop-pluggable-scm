
package pluggable.scm;

import java.util.Properties;

/**
* The SCP provider data store specification.
*/
public interface SCMProviderDataStore {

  /**
  * Return a string representation, in a collection of all the available SCM providers.
  * @return a string representation, in a collection of all the available SCM providers.
  */
  public List<String> getAll();

  /**
  * Return properties object for the specified SCM provider id.
  * @param id - Unique SCM provider implementation id.
  * @return properties object for the specified SCM provider id.
  */
  public Properties get(String id);
}
