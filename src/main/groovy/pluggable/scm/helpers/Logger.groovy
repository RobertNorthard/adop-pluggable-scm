package pluggable.scm.helpers;

import pluggable.configuration.EnvVarProperty;

/**
* A helper class for logging.
*/
public class Logger {

  private static final EnvVarProperty envVarProperty = EnvVarProperty.getInstance();

  /**
  * Print to the stdout logger with a INFO presecence.
  *
  *  @param msg the message to log.
  */
  public static void info(String msg){
    log("INFO", msg);
  }

  /**
  * Print to the stdout logger with the specified log level and message.
  *
  * @param logLvl the logLvl e.g. INFO
  * @param msg the message to log.
  */
  public static void log(String logLvl, String msg){
    envVarProperty.getLogger().println("[" + logLvl + "] - " +  msg);
  }
}
