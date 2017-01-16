package pluggable.scm.bitbucket;

/**
* Set of named constants representing the supported Gerrit SCM protocols.
*/
enum BitbucketSCMProtocol {
    SSH("ssh"),
    HTTP("http"),
    HTTPS("https")

    private final String protocol = "";

    /**
    * Constructor for class GerritSCMProtocol.
    *
    * @param protocal a string representation of the protocol e.g. ssh, https
    */
    public BitbucketSCMProtocol(String protocol) {
        this.protocol = protocol;
    }

    public static void isProtocolSupported(BitbucketSCMProtocol protocol){
      switch(protocol){
        case BitbucketSCMProtocol.SSH:
        case BitbucketSCMProtocol.HTTP:
        case BitbucketSCMProtocol.HTTPS:
          break;
        default:
          throw new IllegalArgumentException("SCM Protocol type not supported.");
          break;
      }
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
