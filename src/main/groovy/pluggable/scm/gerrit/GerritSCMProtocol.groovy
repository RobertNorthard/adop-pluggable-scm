
package pluggable.scm.gerrit;

/**
* Set of named constants representing the supported Gerrit SCM protocols.
*/
enum GerritSCMProtocol {
    SSH("ssh"),
    HTTP("http"),
    HTTPS("https")

    private final String protocol = "";

    /**
    * Constructor for class GerritSCMProtocol.
    *
    * @param protocal a string representation of the protocol e.g. ssh, https
    */
    public GerritSCMProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
    * Return a string representation of the SCM protocol.
    * @return a string representation of the SCM protocol.
    */
    @Override
    public String toString(){
      return this.protocol;
    }
}
