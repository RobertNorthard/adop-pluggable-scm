package pluggable.scm.helpers;

/**
* Set of named constants representing the supported log levels.
*/
enum LogLevel {
    INFO("INFO"),
    DEBUG("DEBUG"),
    ERROR("ERROR")

    private final String logLvl = "";

    /**
    * Constructor for class LogLevel.
    *
    * @param logLvl a string representation of the protocol e.g. ssh, https
    */
    public LogLevel(String logLvl) {
        this.logLvl = logLvl;
    }

    /**
    * Return a string representation of the log level.
    * @return a string representation of the log level.
    */
    @Override
    public String toString(){
      return this.logLvl;
    }
}
