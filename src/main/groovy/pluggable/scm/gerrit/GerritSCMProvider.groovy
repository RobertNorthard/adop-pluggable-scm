
package pluggable.scm.gerrit;

import pluggable.scm.SCMProvider;

/**
* This class implements the Gerrit SCM Provider.
*
*/
public class GerritSCMProvider implements SCMProvider {

  private final String scmUrl = "";
  private final int scmPort = 0;
  private final GerritSCMProtocol scmProtocol = null;

  private final String scmGerritCloneUser = "";
  private final String scmGerritServerProfile = "";

  /**
  * Constructor for class GerritSCMProvider.
  *
  * @param scmUrl scm url e.g. 10.0.0.1, gerrit
  * @param scmPort scm port
  * @param scmProtocol scm clone protocol
  * @param scmGerritProfile scm Gerrit profile
  * @param scmGerritCloneUser scm gerrit clone user. Must be set of the SCM protocol is set to SSH.
  *
  * @throws IllegalArgumentException
  *         If SCM protocol is equal to GerritSCMProtocol.SSH and the Gerrit clone user has not been provided.
  *         If Gerrit server profile is not set.
  */
  public GerritSCMProvider(String scmUrl, int scmPort,
    GerritSCMProtocol scmProtocol, String scmGerritServerProfile, String scmGerritCloneUser, Boolean scmCodeReviewEnabled){

      this.scmUrl = scmUrl;
      this.scmPort = scmPort;
      this.scmProtocol = scmProtocol;
      this.scmCodeReviewEnabled = scmCodeReviewEnabled;

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
  * Helper class to convert string input into executable bash commands.
  *
  * @param command The bash command you want to execute input as a string
  *
  */
  public class ExecuteShellCommand {

  public String executeCommand(String command) {

      StringBuffer output = new StringBuffer();

      Process p;
      try {
          p = Runtime.getRuntime().exec(command);
          p.waitFor();
          BufferedReader reader = 
                          new BufferedReader(new InputStreamReader(p.getInputStream()));

          String line = "";           
          while ((line = reader.readLine())!= null) {
              output.append(line + "\n");
          }

      } catch (Exception e) {
          e.printStackTrace();
      }

      return output.toString();

  }

  }

  /**
  * Return Gerrit SCM URL.
  * @return SCM url for the provider.
  *     e.g. Gerrit-SSH  ssh://jenkins@10.0.0.0:22/
  *          Gerrit-HTTP http://10.0.0.0:80/
  *
  * @throws IllegalArgumentException
  *           If the SCM protocol type is not supported.
  */
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

      url.append(this.scmUrl);
      url.append(":");
      url.append(this.scmPort);
      url.append("/");

      return url;
  }

  public createScmRepos(String workspace, String cartridgeFolder, String projectFolderName, String overwriteRepos) {

    ExecuteShellCommand com = new ExecuteShellCommand()
    String codeReview = null;
    String permissions_repo = null;
    String repo_namespace = null;
    
    private static String cartHome = "/cartridge"
    private static String urlsFile = workspace + cartHome + "/src/urls.txt"

    if (this.scmCodeReviewEnabled == "true"){
      println("Adding review to permissions")
      permissions_repo = projectFolderName + "/permissions-with-review"
    } else {
      permissions_repo = projectFolderName + "/permissions"
    }

    println(permissions_repo)


    if (cartridgeFolder == ""){
      println("Folder name not specified...")
      repo_namespace = projectFolderName
    } else {
      println ("Folder name specified, changing project namespace value..")
      repo_namespace = projectFolderName + "/" + cartridgeFolder
    }

    // Create repositories
    String command1 = "cat " + urlsFile
    List<String> repoList = new ArrayList<String>();
    repoList = (com.executeCommand(command1).split("\\r?\\n"));

    for(String repo: repoList) {
        String repoName = repo.substring(repo.lastIndexOf("/") + 1, repo.indexOf(".git"));
        target_repo_name= repo_namespace + "/" + repoName
        int repo_exists=0;
        
        // Check if the repository already exists or not
        String listCommand = "ssh -n -o StrictHostKeyChecking=no -p " + this.gerritPort + " " + this.gerritUser + "@" + this.gerritEndpoint + " gerrit ls-projects --type code"
        gerritRepoList = (com.executeCommand(listCommand).split("\\r?\\n"));
        
        for(String gerritRepo: gerritRepoList) {
          if(gerritRepo.trim().contains(target_repo_name)) {
             println("Found: " + target_repo_name);
             repo_exists=1
             break
          }
        }
            
        // If not, create it
        if (repo_exists.equals(0)) {
          String createCommand = "ssh -n -o StrictHostKeyChecking=no -p " + this.gerritPort + " " + this.gerritUser + "@" + this.gerritEndpoint + " gerrit create-project --parent " + permissions_repo + " " + target_repo_name
          com.executeCommand(createCommand)
        } else{
          println("Repository already exists, skipping create: " + target_repo_name)
        }
        
        // Populate repository
        String tempDir = workspace + "/tmp"
        String cloneCommand = "git clone ssh://" + this.gerritUser + "@" + this.gerritEndpoint + ":" + this.gerritPort + "/" + target_repo_name + " " + tempDir
        String gitDir = "--git-dir=" + tempDir + "/" + target_repo_name + "/.git"
        String fetchCommand = "git " + gitDir + " remote add source " + repo + " && git " + gitDir + " fetch source"
        com.executeCommand(cloneCommand)
        com.executeCommand(fetchCommand)
        if (overwriteRepos == "true"){
          String pushCommand = "git " + gitDir + " push origin +refs/remotes/source/*:refs/heads/*"
          com.executeCommand(pushCommand)
        } else {
          String pushCommand = "git " + gitDir + " push origin refs/remotes/source/*:refs/heads/*"
          com.executeCommand(pushCommand)
        }
        
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
