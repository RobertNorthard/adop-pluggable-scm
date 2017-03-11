package pluggable.scm.helpers;

import pluggable.configuration.EnvVarProperty;

/**
* A helper class for logging.
*/
public class Logger {

  private static final EnvVarProperty envVarProperty = EnvVarProperty.getInstance();

  /**
  * Print to the stdout logger with a INFO precedence.
  *
  *  @param msg the message to log.
  */
  public static void info(String msg){
    log(LogLevel.INFO, msg);
  }

  /**
  * Print to the stdout logger with a ERROR precedence.
  *
  *  @param msg the message to log.
  */
  public static void error(String msg){
    log(LogLevel.ERROR, msg);
  }

  /**
  * Print to the stdout logger with the specified log level and message.
  *
  * @param logLvl the logLvl e.g. INFO
  * @param msg the message to log.
  */
  public static void log(LogLevel logLvl, String msg){
    def logger = envVarProperty.getLogger();

    String message = "[" + logLvl.toString() + "] - " +  msg;

    //print to stdout
    if(logger == null){
      println(message);
    }else{
      logger.println(message);
    }
  }

  /**
  * Print to the stdout logger with the specified message.
  *
  * @param msg the message to log.
  */
  public static void log(String msg){
    def logger = envVarProperty.getLogger();

    //print to stdout
    if(logger == null){
      println(msg);
    }else{
      logger.println(msg);
    }
  }
}
