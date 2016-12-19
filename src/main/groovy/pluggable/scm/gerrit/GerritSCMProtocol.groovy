
package pluggable.scm.gerrit;

/**
* Set of named constants representing the supported Gerrit SCM protocols.
*
* @author Robert Northard <robertnorthard@googlemail.com>
*/
enum GerritSCMProtocol {
    SSH("ssh"),
    HTTP("http"),
    HTTPS("https")

    private final String protocal = "";

    public GerritSCMProtocol(String protocal) {
        this.protocal = protocal;
    }

    public String toString(){
      return this.protocal;
    }
}
