package pluggable.scm.helpers;

import pluggable.scm.helpers.Logger;
import pluggable.scm.helpers.LogLevel;

import  java.util.regex.Matcher;
import  java.util.regex.Pattern;

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

      // commands may contain username/passwords.
      Logger.info(sanitizeCommand(command));

      StringBuffer output = new StringBuffer();
      Process process = null;
      String result = "";
      try {
          process = Runtime.getRuntime().exec(command);
          process.waitForProcessOutput(output, output);
          result = output.toString();

          Logger.log(result);

          if(process.exitValue() != 0){
            Logger.error("Command executed with: " + process.exitValue());
          }
      } catch (Exception exception) {
          Logger.error(exception.getMessage());
      }

      return result;
  }

  /**
  * Return a logger friendy version of a command.
  *
  * @param command command to sanitize (e.g. remove username/passwords)
  * @return a logger friendly command.
  */
  public String sanitizeCommand(String command){
    Pattern pattern = Pattern.compile("\\w{1,}:\\w{1,}");
    Matcher matcher = pattern.matcher(command);
    String sanitizedOutput = command;
    while(matcher.find()){
      String sensitiveInput = matcher.group()
      sanitizedOutput = sanitizedOutput.replaceAll(sensitiveInput, "******");
    }

    return sanitizedOutput;
  }
}
