
package pluggable.scm.gerrit;

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
