
package pluggable.scm;

/**
* This interfaces defines the specification of an SCMProvider.
*/
public interface SCMProvider {

    /**
    *  Return a closure representation of SCM section.
    *
    *
    *  @param projectName - name of the project.
    *  @param repoName  - name of the repository to checkout.
    *  @param branchName - name of the branch to checkout.
    *  @param credentialId - name of the credential in the Jenkins credential
    *          manager to use.
    *  @param extras - extra closures to add to the SCM section.
    *  @return a closure representation of the SCM providers SCM section.
    **/
    public Closure get(String projectName, String repoName, String branchName, String credentialId, Closure extras);

    /**
    * Return a closure representation of the SCM providers trigger SCM section.
    *
    * @param projectName - name of the project.
    * @param repoName  - name of the repository to checkout.
    * @param branchName - name of the branch checkout on ref updates.
    * @return closure representation of the SCM providers trigger SCM section.
    */
    public Closure trigger(String projectName, String repoName, String branchName)

    /**
    * Creates the relevant repositories defined by your cartridge in your chosen SCM provider
    *
    * @param workspace Workspace of the cartridge loader job
    * @param namespace Location in your SCM provider where your repositories will be created
    * @param overwriteRepos Whether the contents of your created repositories are over-written or not
    **/
    public void createScmRepos(String workspace, String repoNamespace, String codeReviewEnabled, String overwriteRepos)



    /**
     * Set username and password for scm providers for example bitbucket to access server.
     *
     * @param String Username
     * @param String Password
     **/
    public void setScmProviderCredentials(String username, String password)
}
