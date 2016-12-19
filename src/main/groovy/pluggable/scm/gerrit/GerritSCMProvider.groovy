
package pluggable.scm.gerrit;

import pluggable.scm.SCMProvider;

/**
* This class implements the Gerrit SCM Provider.

* @author Robert Northard <robertnorthard@googlemail.com>
*/
public class GerritSCMProvider implements SCMProvider {

  private String scmProtocol = "";
  private String scmPort = "";
  private String scmUrl = "";

  private String scmGerritCloneUser = "";
  private String scmGerritServerProfile = "";

  public GerritSCMProvider(String scmPort, String scmUrl,
    String scmProtocol, String scmGerritServerProfile, String scmGerritCloneUser ){

      this.scmUrl = scmUrl;
      this.scmPort = scmPort;
      this.scmProtocol = scmProtocol;
      this.scmGerritServerProfile = scmGerritServerProfile;
      this.scmGerritCloneUser = scmGerritCloneUser;
  }

  /**
  * Returns a String representation of the Gerrit server profile name.
  * @return a String representation of the Gerrit server profile name.
  */
  def String getScmGerritProfile(){
    return this.scmGerritServerProfile;
  }

  /**
  * Return SCM url.
  * @return SCM url.
  */
  public String getScmUrl(){

      StringBuffer url = new StringBuffer("")

      url.append(this.scmProtocol + "://");

      switch(this.scmProtocol){
        case "ssh":
          url.append(this.scmGerritCloneUser + "@");
          break;

        case "http":
        case "https":
          // don't do anything
          break;

        default:
          throw new IllegalArgumentException("SCM Protocal type not supported.");
          break;
      }

      url.append(this.scmUrl + ":" + this.scmPort + "/");

      return url;
  }

  /**
    Return SCM section.

    @param projectName - name of the project.
    @param repoName  - name of the repository to clone.
    @param branchName - name of branch.
    @param credentialId - name of the credential in the Jenkins credential
            manager to use.
    @param extras - extra closures to add to the SCM section.
    @return closure representation of the SCM providers SCM section.
  **/
  public Closure get(String projectName, String repoName, String branchName, String credentialId, Closure extras){
    if(extras == null) extras = {}
        return {
            git extras >> {
              remote{
                url(this.getScmUrl() + projectName + "/" + repoName)
                credentials(credential_id)
              }
              branch("*/"+ branchName)
            }
        }
    }

    /**
    * Return a closure representation of the SCM providers trigger SCM section.
    *
    * @param projectName - project name.
    * @param repoName - repository name.
    * @param branchName - branch name to trigger.
    * @return closure representation of the SCM providers trigger SCM section.
    */
    public Closure trigger(String projectName, String repoName, String branchName) {
        return {
          gerrit {
            events {
                refUpdated()
            }
            project(projectName + '/' + repoName, 'plain:' + branchName)
            configure { node ->
                node / serverName(this.getScmGerritProfile())
            }
          }
        }
    }
}
