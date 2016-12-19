
package pluggable.scm;

/**
* This interfaces defines the specification of an SCMProvider.
*
* @author Robert Northard <robertnorthard@googlemail.com>
*/
public interface SCMProvider {

    /**
    *  Return SCM section.
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
}
