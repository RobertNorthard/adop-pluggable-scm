
package pluggable.scm;

public interface SCMProvider {

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
    Closure get(String projectName, String repoName, String branchName, String credentialId, Closure extras);
}
