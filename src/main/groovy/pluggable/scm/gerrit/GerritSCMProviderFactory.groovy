
package pluggable.scm.gerrit;

import pluggable.scm.SCMProvider;
import pluggable.scm.SCMProviderFactory;

/**
* This class a Gerrit SCM Porivder factory class which is responsible for parsing the
* providers properties and creating a GerritSCMProvider.
* 
* @author Robert Northard <robertnorthard@googlemail.com>
*/
public class GerritSCMProviderFactory implements SCMProviderFactory {

  /**
  * Return a SCM Provider configured according to the provided SCM properties.
  *
  * @param scmProviderProperties - properties for the SCM provider.
  * @return SCMProvider configured from the provided SCM properties.
  **/
  public SCMProvider create(Properties scmProviderProperties){

    GerritSCMProvider scmProvider = null;

    String scmUrl = scmProviderProperties.getProperty("scm.url");
    String scmProtocol = scmProviderProperties.getProperty("scm.protocol");
    int scmPort = Integer.parseInt(scmProviderProperties.getProperty("scm.port"));

    String scmGerritProfile = scmProviderProperties.getProperty("scm.gerrit.server.profile");
    String scmGerritCloneUser = scmProviderProperties.getProperty("scm.gerrit.clone.user");

    scmProvider = new GerritSCMProvider(scmUrl, scmPort,GerritSCMProtocol.valueOf(scmProtocol.toUpperCase()), scmGerritProfile, scmGerritCloneUser);

    return scmProvider;
  }
}
