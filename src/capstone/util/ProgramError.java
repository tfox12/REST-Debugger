package capstone.util;

public class ProgramError
{
    public int lineNumber;
    public String errorText;

    public ProgramError(int lineNumber, String errorText)
    {
        this.lineNumber = lineNumber;
        this.errorText = errorText;
    }
}

