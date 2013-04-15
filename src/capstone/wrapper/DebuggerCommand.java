package capstone.wrapper;

import static capstone.wrapper.DebuggerCommand.RequestParameters.*;

public enum DebuggerCommand
{
    PREPARE('p', "prepare", BOTH)
  , RUN('r', "run", NEITHER)
  , STEPIN('z', "stepin", NEITHER)
  , STEPOUT('w', "stepout", NEITHER)
  , STEPOVER('q', "stepover", NEITHER)
  , GETVALUES('V', "getvalues", RETURNS_RESULT)
  , GETSTDOUT('o', "getstdout", RETURNS_RESULT)
  , GETSTDERR('e', "getstderr", RETURNS_RESULT)
  , GIVEINPUT('i', "giveinput", NEEDS_DATA)
  , ADDBREAKPOINT('b', "breakpoint", NEEDS_DATA)
  , GETLINENUMBER('L', "linenumber", RETURNS_RESULT)
  , KILLDEBUGGER('k', "killdebugger", NEITHER)
  , UNKNOWN('u', "unknown", NEITHER)
  ;
    public enum RequestParameters
    {
        NEITHER
      , NEEDS_DATA
      , RETURNS_RESULT
      , BOTH
    }

    private char commandCharacter;
    private String longFormCommand;
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

    DebuggerCommand(char c, String longFormCommand, RequestParameters parameters)
    {
        this.commandCharacter = c;
        this.longFormCommand = longFormCommand;

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

    public String getString()
    {
        return longFormCommand;
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

    public static DebuggerCommand fromString(String s)
    {
        for (DebuggerCommand cmd : DebuggerCommand.values())
        {
            if (s.equals(cmd.longFormCommand))
                return cmd;
        }
        return UNKNOWN;
    }
}

