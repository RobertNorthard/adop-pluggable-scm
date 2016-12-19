
package pluggable.scm.gerrit;

import pluggable.scm.SCMProvider;
import pluggable.scm.SCMProviderFactory;

public class GerritSCMProviderFactory implements SCMProviderFactory {

  /**
  * Return a SCM Provider configured according to the provided SCM properties.
  *
  * @param scmProviderProperties - properties for the SCM provider.
  * @return SCMProvider configured from the provided SCM properties.
  **/
  public SCMProvider create(Properties scmProviderProperties){

    GerritSCMProvider scmProvider = null;

    return scmProvider;
  }
}
