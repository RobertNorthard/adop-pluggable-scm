package pluggable.scm.helpers;

/**
* Helper class to convert string input into executable bash commands.
*/
public class ExecuteShellCommand {

  /**
  * Execute arbitary shell commands.
  * @param command command to execute.
  *
  * Source: http://stackoverflow.com/questions/26830617/java-running-bash-commands
  */
  public String executeCommand(String command) {

      if (command == null || command.equals("")){
        throw new IllegalArgumentException("A command must be provided.")
      }

      StringBuffer output = new StringBuffer();
      Process p = null;

      try {
          p = Runtime.getRuntime().exec(command);
          p.waitFor();

          BufferedReader reader =
              new BufferedReader(
                new InputStreamReader(p.getInputStream()));

          String line = "";

          while ((line = reader.readLine()) != null) {
              output.append(line + "\n");
          }
      } catch (Exception ex) {
          throw ex;
      }

      return output.toString();
  }
}
