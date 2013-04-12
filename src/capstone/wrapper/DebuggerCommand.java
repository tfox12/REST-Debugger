package capstone.wrapper;

public enum DebuggerCommand
{
    PREPARE('p', true)
  , RUN('r')
  , STEPIN('z')
  , STEPOUT('w')
  , STEPOVER('q')
  , GETVALUES('V')
  , GETSTDOUT('o')
  , GETSTDERR('e')
  , GIVEINPUT('i', true)
  , ADDBREAKPOINT('b', true)
  , GETLINENUMBER('L')
  , KILLDEBUGGER('k')
  , UNKNOWN('u')
  ;

    private char commandCharacter;
    private boolean hasData;
    private String data;

    DebuggerCommand(char c)
    {
        this.commandCharacter = c;
        this.hasData = false;
    }

    DebuggerCommand(char c, boolean hasData)
    {
        this.commandCharacter = c;
        this.hasData = hasData;
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

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }
}

