package capstone.wrapper;

public enum DebuggerCommand
{
    PREPARE('p')
  , RUN('r')
  , STEPIN('z')
  , STEPOUT('w')
  , STEPOVER('q')
  , GETVALUES('V')
  , GETSTDOUT('o')
  , GETSTDERR('e')
  , GIVEINPUT('i')
  , ADDBREAKPOINT('b')
  , GETLINENUMBER('L')
  , KILLDEBUGGER('k')
  , UNKNOWN('u')
  ;

    private char commandCharacter;

    DebuggerCommand(char c)
    {
        this.commandCharacter = c;
    }

    public char getChar()
    {
        return commandCharacter;
    }

    public static DebuggerCommand fromChar(char c)
    {
        for (DebuggerCommand cmd : DebuggerCommand.values())
        {
            if (c == cmd.commandCharacter)
                return cmd;
        }
        return UNKNOWN;
    }
}

