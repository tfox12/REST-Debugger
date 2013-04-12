package capstone.wrapper;

import capstone.util.*;
import java.util.*;

public abstract class Wrapper
{
    public abstract List<ProgramError> prepare(String programText);
    public abstract void killDebugger();
    public abstract void runProgram();

    public abstract String getStdOut();
    public abstract void provideInput(String input);
    public abstract StackFrame getLocalValues();
    public abstract List<StackFrame> getStack();
    public abstract String evaluateExpression(String expression);

    public abstract void stepIn();
    public abstract void stepOut();
    public abstract void stepOver();

    public abstract void addBreakpoint(int lineNumber);
    public abstract int getLineNumber();

    static void runWrapper(Wrapper wrapper)
    {

    }
}

// TODO the run debugger functionality which takes a Wrapper and launches the timer,
// handles the receive commands, etc. This should be a static function of Wrapper.

