package capstone.wrapper;

import capstone.util.*;
import java.util.*;

public interface Wrapper
{
    public List<ProgramError> prepare(String programText);
    public void killDebugger();
    public void runProgram();

    public String getStdOut();
    public void provideInput(String input);
    public StackFrame getLocalValues();
    public List<StackFrame> getStack();
    public String evaluateExpression(String expression);

    public void stepIn();
    public void stepOut();
    public void stepOver();

    public void addBreakpoint(int lineNumber);
    public int getLineNumber();
}

// TODO the run debugger functionality which takes a Wrapper and launches the timer,
// handles the receive commands, etc. This should be a static function of Wrapper.

// TODO enum of all the debugger commands.
// see: http://docs.oracle.com/javase/tutorial/java/javaOO/enum.html

