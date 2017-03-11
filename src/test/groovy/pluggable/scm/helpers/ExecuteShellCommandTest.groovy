
import pluggable.scm.helpers.ExecuteShellCommand;

public class ExecuteShellCommandTest extends GroovyTestCase {

  public void testExecuteCommandNull(){
    shouldFail(IllegalArgumentException){
      ExecuteShellCommand command = new ExecuteShellCommand();
      command.executeCommand(null);
    }
  }

  public void testExecuteCommandEmptyCommand(){
    shouldFail(IllegalArgumentException){
      ExecuteShellCommand command = new ExecuteShellCommand();
      command.executeCommand("");
    }
  }

  public void testSanitizeCommandUnsafeInputGitCloneHttps(){

    ExecuteShellCommand command = new ExecuteShellCommand();

    assertEquals  "git clone https://******@github.com/Accenture/adop-pluggable-scm.git",
      command.sanitizeCommand("git clone https://dummy:dummy@github.com/Accenture/adop-pluggable-scm.git");
  }

  public void testSanitizeCommandUnsafeInputGitCloneSsh(){

    ExecuteShellCommand command = new ExecuteShellCommand();

    assertEquals  "git clone ssh://******@github.com/Accenture/adop-pluggable-scm.git",
      command.sanitizeCommand("git clone ssh://d:d@github.com/Accenture/adop-pluggable-scm.git");
  }

  public void testSanitizeCommandSafeInputGitCloneHttp(){

    ExecuteShellCommand command = new ExecuteShellCommand();

    assertEquals  "git clone https://github.com/Accenture/adop-pluggable-scm.git",
      command.sanitizeCommand("git clone https://github.com/Accenture/adop-pluggable-scm.git");
  }

  public void testSanitizeCommandSafeInputGitCloneSsh(){

    ExecuteShellCommand command = new ExecuteShellCommand();

    assertEquals  "git clone ssh://github.com/Accenture/adop-pluggable-scm.git",
      command.sanitizeCommand("git clone ssh://github.com/Accenture/adop-pluggable-scm.git");
  }

  public void testSanitizeCommandSafeInputListFiles(){

    ExecuteShellCommand command = new ExecuteShellCommand();

    assertEquals  "ls -la",
      command.sanitizeCommand("ls -la");
  }

  public void testSanitizeCommandSafeInputGitPushOriginRefs(){

    ExecuteShellCommand command = new ExecuteShellCommand();

    assertEquals  "git push origin +refs/remotes/source/*:refs/heads/*",
      command.sanitizeCommand("git push origin +refs/remotes/source/*:refs/heads/*");
  }
}
