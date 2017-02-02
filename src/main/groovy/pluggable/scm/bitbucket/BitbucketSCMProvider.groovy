package pluggable.scm.bitbucket;

import pluggable.scm.SCMProvider;
import pluggable.configuration.EnvVarProperty;
import pluggable.scm.helpers.*;
import java.util.Properties;

/**
* This class implements the Bitbucket SCM Provider.
*/
public class BitbucketSCMProvider implements SCMProvider {


  // SCM specific variables.
  private final int scmPort;
  private final BitbucketSCMProtocol scmProtocol;

  // Bitbucket specific variables.
  private final String bitbucketHost;
  private final String bitbucketEndpoint;
  private final int bitbucketPort;
  private final BitbucketSCMProtocol bitbucketProtocol;
  private final String bitbucketUsername;
  private final String bitbucketPassword;

  /**
  * Constructor for class BitbucketSCMProvider.
  *
  * @param scmPort scm port
  * @param scmProtocol scm clone protocol
  * @param bitbucketHost host url e.g. 10.0.0.1, innersource.accenture.com
  * @param gerritEndpoint bitbucket host endpoint.
  * @param bitbucketProtocol protocol which will be used for HTTP requests.
  * @param bitbucketPort bitbucket API port.
  */
  public BitbucketSCMProvider(int scmPort,
                              BitbucketSCMProtocol scmProtocol,
                              String bitbucketHost,
                              String bitbucketEndpoint,
                              BitbucketSCMProtocol bitbucketProtocol,
                              int bitbucketPort){

      this.scmPort = scmPort;
      this.scmProtocol = scmProtocol;
      this.bitbucketHost = bitbucketHost;
      this.bitbucketEndpoint = bitbucketEndpoint;
      this.bitbucketPort = bitbucketPort;
      this.bitbucketProtocol = bitbucketProtocol;

      // If not it will thorw IllegalArgumentException.
      BitbucketSCMProtocol.isProtocolSupported(this.bitbucketProtocol);

      EnvVarProperty envVarProperty = EnvVarProperty.getInstance();
      String filePath =  envVarProperty.getProperty("WORKSPACE")+envVarProperty.getProperty("SCM_KEY")
      Properties fileProperties = HelperUtils.getFileProperties(filePath)
      this.bitbucketUsername = fileProperties.getProperty("SCM_USERNAME");
      this.bitbucketPassword = fileProperties.getProperty("SCM_PASSWORD");
  }

    /**
     * Return Bitbucket SCM URL.
     * @return SCM url for the provider.
     *     e.g. Bitbucket-SSH  ssh://jenkins@10.0.0.0:22/
     *          Bitbucket-HTTP http://10.0.0.0:80/scm/
     *          Bitbucket-HTTPS http://10.0.0.0:443/scm/
     *
     * @throws IllegalArgumentException
     *           If the SCM protocol type is not supported.
     **/

      public String getScmUrl(){
          return this.getScmUrl(null, null);
      }

      /**
      * Return Bitbucket SCM URL.
      * @param String username
      * @param String password
      * @return SCM url for the provider.
      *     e.g. Bitbucket-SSH  ssh://jenkins@10.0.0.0:22/
      *          Bitbucket-HTTP http://10.0.0.0:80/scm/
      *          Bitbucket-HTTPS http://10.0.0.0:443/scm/
      *
      * @throws IllegalArgumentException
      *           If the SCM protocol type is not supported.
      **/
      public String getScmUrl(String username, String password){

          StringBuffer url = new StringBuffer("")

          url.append(this.scmProtocol);
          url.append("://");

          switch(this.scmProtocol){
            case BitbucketSCMProtocol.SSH:
              url.append("git@");
              break;
            case BitbucketSCMProtocol.HTTP:
            case BitbucketSCMProtocol.HTTPS:
                if(username != null && password != null){
                    url.append(username);
                    url.append(":");
                    url.append(password);
                    url.append("@");
                }
              break;
            default:
              throw new IllegalArgumentException("SCM Protocol type not supported.");
              break;
          }

          url.append(this.bitbucketHost);
          url.append(":");
          url.append(this.scmPort);
          url.append(this.bitbucketEndpoint);
          if(this.bitbucketEndpoint != "/"){
            url.append("/")
          }
          if((this.scmProtocol == BitbucketSCMProtocol.HTTP) ||
             (this.scmProtocol == BitbucketSCMProtocol.HTTPS)
          ){
            url.append("scm/");
          }

          return url;
      }

  /**
  * Creates relevant repositories defined by your cartridge in your chosen SCM provider
  * @param workspace Workspace of the cartridge loader job
  * @param repoNamespace Location in your SCM provider where your repositories will be created
  * @parma overwriteRepos
  **/
  public void createScmRepos(String workspace, String repoNamespace, String codeReviewEnabled, String overwriteRepos) {
    String bitbucketUrl = this.bitbucketProtocol.toString() + "://" + this.bitbucketHost + ":" + this.bitbucketPort + this.bitbucketEndpoint;

    BitbucketRequestUtil.isProjectAvailable(bitbucketUrl, this.bitbucketUsername, this.bitbucketPassword, repoNamespace);

    ExecuteShellCommand com = new ExecuteShellCommand()

    String cartHome = "/cartridge"
    String urlsFile = workspace + cartHome + "/src/urls.txt"
    EnvVarProperty envVarProperty = EnvVarProperty.getInstance();

    // Create repositories
    String command1 = "cat " + urlsFile
    List<String> repoList = new ArrayList<String>();
    repoList = (com.executeCommand(command1).split("\\r?\\n"));

    for(String repo: repoList) {
        String repoName = repo.substring(repo.lastIndexOf("/") + 1, repo.indexOf(".git"));
        String target_repo_name = repoNamespace + "/" + repoName
        int repo_exists=0;

        List<String> bitbucketRepoList = BitbucketRequestUtil.getProjectRepositorys(bitbucketUrl, this.bitbucketUsername, this.bitbucketPassword, repoNamespace);
        for(String bitbucketRepo: bitbucketRepoList) {
          if(bitbucketRepo.trim().contains(repoName)) {
             Logger.info("Found: " + target_repo_name);
             repo_exists=1
             break
          }
        }
        // If not, create it
        if (repo_exists == 0) {
          BitbucketRequestUtil.createRepository(bitbucketUrl, this.bitbucketUsername, this.bitbucketPassword, repoNamespace, repoName);
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
        tempScript << "git clone " + this.getScmUrl(this.bitbucketUsername, this.bitbucketPassword) + repoNamespace + "/" + repoName + ".git " + tempDir + "/" + repoName + "\n"
        def gitDir = "--git-dir=" + tempDir + "/" + repoName + "/.git"
        tempScript << "git " + gitDir + " remote add source " + repo + "\n"
        tempScript << "git " + gitDir + " fetch source" + "\n"

        if (overwriteRepos == "true"){
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
              bitbucketPush()
              scm('')
        }
    }
}
