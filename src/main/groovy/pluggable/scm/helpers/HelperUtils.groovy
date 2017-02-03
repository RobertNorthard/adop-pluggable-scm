package pluggable.scm.helpers;
@GrabResolver(name='jenkins-ci.plugins', root='http://repo.jenkins-ci.org/releases/')
@Grab(group='org.jenkins-ci.plugins', module='credentials', version='2.1.5')
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.common.*;
import java.util.Properties;

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
  *            If credentialId is null or empty.
  */
  public static String[] extractPasswordCredentials(String credentialId){
        if(credentialId == null || credentialId.equals("")){
            throw new IllegalArgumentException("Credential id not valid.");
        }

        def username_matcher = CredentialsMatchers.withId(credentialId);

        def available_credentials = CredentialsProvider.lookupCredentials(
                StandardUsernameCredentials.class
        );

        return [CredentialsMatchers.firstOrNull(available_credentials, username_matcher).username,
                CredentialsMatchers.firstOrNull(available_credentials, username_matcher).password];
  }

  /**
  * Get Properties from a file.
  *
  *  @param String file path.
  *  @return Properties object made from file.
  *  @throw FileNotFoundException
  *            If file doesn't exist.
  */
  public static Properties getFileProperties(String filePath){

        File tempFile = new File(filePath);
        Properties fileProperties = new Properties();

        if (!tempFile.exists()) {
          throw new FileNotFoundException("file doesn't exist");
        }

        InputStream tempInput = new FileInputStream(tempFile);
        fileProperties.load(tempInput);

        return fileProperties;
  }
}
