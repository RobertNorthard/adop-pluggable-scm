package pluggable.scm.bitbucket;
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0-RC2' )
import groovyx.net.http.*;
import static groovyx.net.http.ContentType.*;
import static groovyx.net.http.Method.*;
import pluggable.scm.helpers.*;


public class BitbucketRequestUtil {

  public static void isProjectAvailable(String bitbucketUrl, String username, String password, String projectKey){
    def http = new HTTPBuilder(bitbucketUrl);
    def auth = "${username}:${password}".bytes.encodeBase64().toString()

    http.request(GET){ req ->
        uri.path = "/rest/api/latest/projects/" + projectKey
        headers.'Authorization' = "Basic ${auth}"
        response.success = { resp ->
          assert resp.status == 200
          Logger.info("Project ${projectKey} found all good!")
        };

        response.failure = { resp ->
          if(resp.status == 401){
              Logger.log(LogLevel.ERROR, "Credentials are invalid.");
          }
          if(resp.status > 401){
            throw new HttpResponseException("Project doesn't exist or host not available!");
          }
        };
    };
  }

  public static String[] getProjectRepositorys(String bitbucketUrl, String username, String password, String projectKey){
      def http = new HTTPBuilder(bitbucketUrl);
      def auth = "${username}:${password}".bytes.encodeBase64().toString()
      List<String> repositoryList = [];
      http.request(GET){ req ->
        uri.path = "/rest/api/latest/projects/${projectKey}/repos"
        requestContentType = ContentType.JSON
        headers.'Authorization' = "Basic ${auth}"
        response.success = { resp, json ->
          assert resp.status == 200
          for(int i = 0; i < json.size; i++){
              repositoryList.add(json.values[i].name)
          }
        };

        response.failure = { resp, json ->
          if(resp.status == 404){
              throw new HttpResponseException("URI not found :" + bitbucketUrl + "/rest/api/latest/projects/${projectKey}/repos" + " not found!");
          }else{
              Logger.info(json.errors.message);
          }
        };
      };
      return repositoryList;
  }

  public static void createRepository(String bitbucketUrl, String username, String password, String projectKey, String repoName){
    def http = new HTTPBuilder(bitbucketUrl);
    def auth = "${username}:${password}".bytes.encodeBase64().toString()

    http.request(POST){ req ->
      uri.path = "/rest/api/latest/projects/${projectKey}/repos"
      requestContentType = ContentType.JSON
      body = [name: repoName, scmId: "git", forkable: "true"]
      headers.'Authorization' = "Basic ${auth}"
      response.success = { resp ->
        assert resp.status == 201
        Logger.info("Repository created in Bitbucket : " + projectKey + "/" + repoName);
      };

      response.failure = { resp, json ->
        if(resp.status == 404){
        	throw new HttpResponseException("URI not found :" + bitbucketUrl + "/rest/api/latest/projects/${projectKey}/repos" + " not found!");
        }else{
            Logger.info(json.errors.message);
        }
      };
    };
  }
}
