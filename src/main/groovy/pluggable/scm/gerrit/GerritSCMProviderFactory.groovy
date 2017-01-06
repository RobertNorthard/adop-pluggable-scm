
package pluggable.scm.gerrit;

import pluggable.scm.SCMProvider;
import pluggable.scm.SCMProviderFactory;
import pluggable.scm.SCMProviderInfo;

/**
* The Gerrit SCM factory class is responsible for parsing the
* providers properties and instantiating a GerritSCMProvider.
*/
@SCMProviderInfo(type="gerrit")
public class GerritSCMProviderFactory implements SCMProviderFactory {

  /**
  * A factory method which return an SCM Provider instantiated with the
  * the provided properties.
  *
  * @param scmProviderProperties - properties for the SCM provider.
  * @return SCMProvider configured from the provided SCM properties.
  **/
  public SCMProvider create(Properties scmProviderProperties){

    GerritSCMProvider scmProvider = null;

    String scmHost = scmProviderProperties.getProperty("scm.host");
    String scmProtocol = scmProviderProperties.getProperty("scm.protocol");
    int scmPort = Integer.parseInt(scmProviderProperties.getProperty("scm.port"));

    String scmGerritProfile = scmProviderProperties.getProperty("scm.gerrit.server.profile");
    String scmGerritCloneUser = scmProviderProperties.getProperty("scm.gerrit.ssh.clone.user");
    String scmCodeReviewEnabled = scmProviderProperties.getProperty("scm.code_review.enabled");

    String gerritEndpoint = scmProviderProperties.getProperty("gerrit.endpoint");
    String gerritUser = scmProviderProperties.getProperty("gerrit.user");
    int gerritPort = Integer.parseInt(scmProviderProperties.getProperty("gerrit.port"));

    String gerritPermissions = scmProviderProperties.getProperty("gerrit.permissions.path");
    String gerritPermissionsWithReview = scmProviderProperties.getProperty("gerrit.permissions.with_review.path");

    scmProvider = new GerritSCMProvider(scmHost, scmPort, GerritSCMProtocol.valueOf(scmProtocol.toUpperCase()), scmGerritProfile,
      scmGerritCloneUser, scmCodeReviewEnabled, gerritEndpoint, gerritUser, gerritPort, gerritPermissions, gerritPermissionsWithReview);

    return scmProvider;
  }
}
