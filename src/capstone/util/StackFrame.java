package capstone.util;

import java.util.*;

public class StackFrame
{
    String frameName;
    Integer depth;
    List<ProgramVariable> variables;

    public StackFrame(String frameName, Integer depth, List<ProgramVariable> variables)
    {
        this.frameName = frameName;
        this.depth = depth;
        this.variables = new ArrayList<ProgramVariable>(variables);
    }
}

