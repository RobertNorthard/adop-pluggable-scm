
package pluggable.scm;

import java.util.Properties;

/**
*
*
* @author Robert Northard <robertnorthard@googlemail.com>
*/
public interface SCMProviderDataStore {

  /**
  * Return a string representation, in an array of all the available SCM providers.
  * @return a string representation, in an array of all the available SCM providers.
  */
  public List<String> getAll();

  /**
  * Return properties object for the specified SCM provider id.
  * @param id - Unique SCM provider implementation id.
  * @return properties object for the specified SCM provider id.
  */
  public Properties get(String id);
}
