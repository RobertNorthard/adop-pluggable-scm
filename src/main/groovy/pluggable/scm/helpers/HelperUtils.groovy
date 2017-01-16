package pluggable.scm.helpers;
@GrabResolver(name='jenkins-ci.plugins', root='http://repo.jenkins-ci.org/releases/')
@Grab(group='org.jenkins-ci.plugins', module='credentials', version='2.1.5')
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.common.*;

/**
* A helper class which contain helphull methods.
*/
public class HelperUtils {

  /**
  * Get user name and password from credentialsID.
  *
  *  @param String credentials id.
  *  @return String two values first is username, second password.
  *  @throw IllegalArgumentException
  *            If id is null or empty.
  */
  public static String[] extractCredentials(String id){
        if(id == null || id.equals("")){
            throw new IllegalArgumentException("Credential id not valid.");
        }

        def username_matcher = CredentialsMatchers.withId(id);

        def available_credentials = CredentialsProvider.lookupCredentials(
                StandardUsernameCredentials.class
        );

        return [CredentialsMatchers.firstOrNull(available_credentials, username_matcher, ).username,
                CredentialsMatchers.firstOrNull(available_credentials, username_matcher).password];
  }
}