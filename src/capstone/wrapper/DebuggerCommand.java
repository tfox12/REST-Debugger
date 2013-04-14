package capstone.wrapper;

import static capstone.wrapper.DebuggerCommand.RequestParameters.*;

public enum DebuggerCommand
{
    PREPARE('p', BOTH)
  , RUN('r', NEITHER)
  , STEPIN('z', NEITHER)
  , STEPOUT('w', NEITHER)
  , STEPOVER('q', NEITHER)
  , GETVALUES('V', RETURNS_RESULT)
  , GETSTDOUT('o', RETURNS_RESULT)
  , GETSTDERR('e', RETURNS_RESULT)
  , GIVEINPUT('i', NEEDS_DATA)
  , ADDBREAKPOINT('b', NEEDS_DATA)
  , GETLINENUMBER('L', RETURNS_RESULT)
  , KILLDEBUGGER('k', NEITHER)
  , UNKNOWN('u', NEITHER)
  ;
    public enum RequestParameters
    {
        NEITHER
      , NEEDS_DATA
      , RETURNS_RESULT
      , BOTH
    }

    private char commandCharacter;
    private boolean needsData;
    private boolean returnsResult;

    public boolean needsData()
    {
        return needsData;
    }

    public boolean returnsResult()
    {
        return returnsResult;
    }

    DebuggerCommand(char c, RequestParameters parameters)
    {
        this.commandCharacter = c;

        needsData = false;
        returnsResult = false;

        switch (parameters)
        {
            case NEITHER:
                break;

            case NEEDS_DATA:
                needsData = true;
                break;

            case RETURNS_RESULT:
                returnsResult = true;
                break;

            case BOTH:
                needsData = true;
                returnsResult = true;
                break;

            default:
                break;
        }
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

