
package pluggable.scm.gerrit;

import pluggable.scm.SCMProvider;

import pluggable.configuration.EnvVarProperty;
import pluggable.scm.helpers.ExecuteShellCommand;
import pluggable.scm.helpers.Logger;

/**
* This class implements the Gerrit SCM Provider.
*/
public class GerritSCMProvider implements SCMProvider {

  private final String scmHost = "";
  private final int scmPort = 0;
  private final GerritSCMProtocol scmProtocol = null;

  private final String scmGerritCloneUser = "";
  private final String scmGerritServerProfile = "";
  private final String scmCodeReviewEnabled = "";

  private final String gerritEndpoint = "";
  private final String gerritUser = "";
  private final int gerritPort = 0;

  private final String gerritPermissions = "";
  private final String gerritPermissionsWithReview = "";

  /**
  * Constructor for class GerritSCMProvider.
  *
  * @param scmHost scm url e.g. 10.0.0.1, gerrit.adop.example
  * @param scmPort scm port
  * @param scmProtocol scm clone protocol
  * @param scmGerritProfile scm Gerrit profile
  * @param scmGerritCloneUser scm gerrit clone user. Must be set of the SCM protocol is set to SSH.
  * @param scmCodeReviewEnabled true if code reviewed enabled else false.
  * @param gerritEndpoint gerrit host endpoint.
  * @param gerritUser gerrit API user.
  * @param gerritPort gerrit API port.
  * @param gerritPermissions gerrit permissions repository name.
  * @param gerritPermissionsWithReview gerrit permissions with review repository name.
  *
  * @throws IllegalArgumentException
  *         If SCM protocol is equal to GerritSCMProtocol.SSH and the Gerrit clone user has not been provided.
  *         If Gerrit server profile is not set.
  */
  public GerritSCMProvider(String scmHost, int scmPort, GerritSCMProtocol scmProtocol,
    String scmGerritServerProfile, String scmGerritCloneUser, String scmCodeReviewEnabled,
    String gerritEndpoint, String gerritUser, int gerritPort,
    String gerritPermissions, String gerritPermissionsWithReview){

      this.scmHost = scmHost;
      this.scmPort = scmPort;
      this.scmProtocol = scmProtocol;
      this.scmCodeReviewEnabled = scmCodeReviewEnabled;

      this.gerritEndpoint = gerritEndpoint;
      this.gerritUser = gerritUser;
      this.gerritPort = gerritPort;
      this.gerritPermissions = gerritPermissions;
      this.gerritPermissionsWithReview = gerritPermissionsWithReview;

      if (scmProtocol == GerritSCMProtocol.SSH
            && ( scmGerritCloneUser == null || scmGerritCloneUser.equals(""))){
        throw new IllegalArgumentException("The Gerrit SCM clone user must be set when using the SSH protocol.");
      }else{
        this.scmGerritCloneUser = scmGerritCloneUser;
      }

      if(scmGerritServerProfile.equals("") || scmGerritServerProfile.equals(null) ){
        throw new IllegalArgumentException("The Gerrit SCM profile name must be set to use this SCM provider.");
      } else{
        this.scmGerritServerProfile = scmGerritServerProfile;
      }
  }

  /**
  * Returns a String representation of the Gerrit server profile name.
  * @return a String representation of the Gerrit server profile name.
  */
  def String getScmGerritProfile(){
    return this.scmGerritServerProfile;
  }


  /**
  * Return Gerrit SCM URL.
  * @return SCM url for the provider.
  *     e.g. Gerrit-SSH  ssh://jenkins@10.0.0.0:22/
  *          Gerrit-HTTP http://10.0.0.0:80/
  *
  * @throws IllegalArgumentException
  *           If the SCM protocol type is not supported.
  **/
  public String getScmUrl(){

      StringBuffer url = new StringBuffer("")

      url.append(this.scmProtocol);
      url.append("://");

      switch(this.scmProtocol){
        case GerritSCMProtocol.SSH:
          url.append(this.scmGerritCloneUser);
          url.append("@");
          break;

        case GerritSCMProtocol.HTTP:
        case GerritSCMProtocol.HTTPS:
          //do nothing
          break;

        default:
          throw new IllegalArgumentException("SCM Protocol type not supported.");
          break;
      }

      url.append(this.scmHost);
      url.append(":");
      url.append(this.scmPort);
      url.append("/");

      return url;
  }

  /**
  * Creates relevant repositories defined by your cartridge in your chosen SCM provider
  * @param workspace Workspace of the cartridge loader job
  * @param namespace Location in your SCM provider where your repositories will be created
  * @param overwriteRepos Whether the contents of your created repositories are over-written or not
  **/
  public void createScmRepos(String workspace, String repoNamespace, String codeReviewEnabled, String overwriteRepos) {

    ExecuteShellCommand com = new ExecuteShellCommand()
    String permissions_repo = null;
    String permissions_repo_temp = null;

    String cartHome = "/cartridge"
    String urlsFile = workspace + cartHome + "/src/urls.txt"

    EnvVarProperty envVarProperty = EnvVarProperty.getInstance();

    // Check if code review has been enabled
    if(codeReviewEnabled.equals("true") && this.scmCodeReviewEnabled.equals("false")){
      throw new IllegalArgumentException("You have tried to use code review however it is not supported for your chosen SCM provider.");
    }

    if (codeReviewEnabled.equals("true")){
      permissions_repo_temp = this.gerritPermissionsWithReview
    } else {
      permissions_repo_temp = this.gerritPermissions
    }
    permissions_repo = envVarProperty.returnValue(permissions_repo_temp);

    // Create repositories
    String command1 = "cat " + urlsFile
    List<String> repoList = new ArrayList<String>();
    repoList = (com.executeCommand(command1).split("\\r?\\n"));

    for(String repo: repoList) {
        String repoName = repo.substring(repo.lastIndexOf("/") + 1, repo.indexOf(".git"));
        String target_repo_name= repoNamespace + "/" + repoName
        int repo_exists=0;

        // Check if the repository already exists or not
        String listCommand = "ssh -i " + envVarProperty.getSshPrivateKeyPath() + " -n -o StrictHostKeyChecking=no -p " + this.gerritPort + " " + this.gerritUser + "@" + this.gerritEndpoint + " gerrit ls-projects --type code"
        List<String> gerritRepoList = (com.executeCommand(listCommand).split("\\r?\\n"));

        for(String gerritRepo: gerritRepoList) {
          if(gerritRepo.trim().contains(target_repo_name)) {
             Logger.info("Found: " + target_repo_name);
             repo_exists=1
             break
          }
        }

        // If not, create it
        if (repo_exists.equals(0)) {
          String createCommand = "ssh -i " + envVarProperty.getSshPrivateKeyPath() + " -n -o StrictHostKeyChecking=no -p " + this.gerritPort + " " + this.gerritUser + "@" + this.gerritEndpoint + " gerrit create-project --parent " + permissions_repo + " " + target_repo_name
          com.executeCommand(createCommand)
          Logger.info("Creating repository in Gerrit: " + target_repo_name);
        } else{
          Logger.info("Repository already exists, skipping create: : " + target_repo_name);
        }

        // Populate repository
        String tempDir = workspace + "/tmp"

        def gitSsh = new File (tempDir + '/git_ssh.sh')
        def tempScript = new File(tempDir + '/shell_script.sh')

        gitSsh << "#!/bin/sh\n"
        gitSsh << "exec ssh -i " + envVarProperty.getSshPrivateKeyPath() + " -o StrictHostKeyChecking=no \"\$@\""

        tempScript << "export GIT_SSH=\""+ tempDir + "/git_ssh.sh\"\n"
        tempScript << "git clone ssh://" + this.gerritUser + "@" + this.gerritEndpoint + ":" + this.gerritPort + "/" + target_repo_name + " " + tempDir + "/" + repoName + "\n"
        def gitDir = "--git-dir=" + tempDir + "/" + repoName + "/.git"
        tempScript << "git " + gitDir + " remote add source " + repo + "\n"
        tempScript << "git " + gitDir + " fetch source" + "\n"

        if (overwriteRepos.equals("true")){
          tempScript << "git " + gitDir + " push origin +refs/remotes/source/*:refs/heads/*\n"
          Logger.info("Repository already exists, overwriting: : " + target_repo_name);
        } else {
          tempScript << "git " + gitDir + " push origin refs/remotes/source/*:refs/heads/*\n"
        }

        com.executeCommand('chmod +x ' + tempDir + '/git_ssh.sh')
        com.executeCommand('chmod +x ' + tempDir + '/shell_script.sh')
        com.executeCommand(tempDir + '/shell_script.sh')

        // delete temp scripts.
        gitSsh.delete()
        tempScript.delete()
    }
  }

  /**
    Return SCM section.

    @param projectName - name of the project.
    @param repoName  - name of the repository to clone.
    @param branchName - name of branch.
    @param credentialId - name of the credential in the Jenkins credential
            manager to use.
    @param extras - extra closures to add to the SCM section.
    @return a closure representation of the SCM providers SCM section.
  **/
  public Closure get(String projectName, String repoName, String branchName, String credentialId, Closure extras){
    if(extras == null) extras = {}
        return {
            git extras >> {
              remote{
                url(this.getScmUrl() + projectName + "/" + repoName)
                credentials(credentialId)
              }
              branch(branchName)
            }
        }
    }

    /**
    * Return a closure representation of the SCM providers trigger SCM section.
    *
    * @param projectName - project name.
    * @param repoName - repository name.
    * @param branchName - branch name to trigger.
    * @return a closure representation of the SCM providers trigger SCM section.
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
