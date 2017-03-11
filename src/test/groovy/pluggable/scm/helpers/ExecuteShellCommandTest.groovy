
import pluggable.scm.helpers.ExecuteShellCommand;

public class ExecuteShellCommandTest extends GroovyTestCase {

  public void testReplacement(){

    ExecuteShellCommand command = new ExecuteShellCommand();

    assertEquals  "git clone https://******@github.com/Accenture/adop-pluggable-scm.git",
      command.sanitizeCommand("git clone https://dummy:dummy@github.com/Accenture/adop-pluggable-scm.git");
  }

  public void testReplacementSecond(){

    ExecuteShellCommand command = new ExecuteShellCommand();

    assertEquals  "git clone https://******@github.com/Accenture/adop-pluggable-scm.git",
      command.sanitizeCommand("git clone https://d:d@github.com/Accenture/adop-pluggable-scm.git");
  }

  public void testSafeInputHttp(){

    ExecuteShellCommand command = new ExecuteShellCommand();

    assertEquals  "git clone https://github.com/Accenture/adop-pluggable-scm.git",
      command.sanitizeCommand("git clone https://github.com/Accenture/adop-pluggable-scm.git");
  }

  public void testSafeInputSsh(){

    ExecuteShellCommand command = new ExecuteShellCommand();

    assertEquals  "git clone ssh://github.com/Accenture/adop-pluggable-scm.git",
      command.sanitizeCommand("git clone ssh://github.com/Accenture/adop-pluggable-scm.git");
  }

  public void testSafeInputListFiles(){

    ExecuteShellCommand command = new ExecuteShellCommand();

    assertEquals  "ls -la",
      command.sanitizeCommand("ls -la");
  }


  public void testSafeInputGitPushOriginRefs(){

    ExecuteShellCommand command = new ExecuteShellCommand();

    assertEquals  "git push origin +refs/remotes/source/*:refs/heads/*",
      command.sanitizeCommand("git push origin +refs/remotes/source/*:refs/heads/*");
  }

  public void testUnsafeInputGitRemoteAdd(){

    ExecuteShellCommand command = new ExecuteShellCommand();

    assertEquals  "git remote add upstream https://******@github.com/Accenture/adop-pluggable-scm.git",
      command.sanitizeCommand("git remote add upstream https://d:d@github.com/Accenture/adop-pluggable-scm.git");
  }

  public void testExecuteCommand(){

    ExecuteShellCommand command = new ExecuteShellCommand();

    assertEquals  "",
      command.executeCommand("somecommandthatdoesnotexist");
  }

  public void testExecuteCommandNonZeroExitCode(){

    ExecuteShellCommand command = new ExecuteShellCommand();

    assertEquals  "",
      command.executeCommand("grep thegodfather README.md");
  }
}
