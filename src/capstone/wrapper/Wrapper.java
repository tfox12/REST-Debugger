package capstone.wrapper;

import capstone.util.*;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public abstract class Wrapper
{
    private Object commandLock;
    private DebuggerCommand command;

    public Wrapper()
    {
        commandLock = new Object();
    }

    public abstract List<ProgramError> prepare(String programText) throws IOException, InterruptedException;
    public abstract void killDebugger();
    public abstract void runProgram() throws IOException;

    public abstract String getStdOut() throws IOException;
    public abstract void provideInput(String input) throws IOException;
    public abstract StackFrame getLocalValues() throws IOException;
    public abstract List<StackFrame> getStack() throws IOException;
    public abstract String evaluateExpression(String expression) throws IOException;

    public abstract void stepIn() throws IOException;
    public abstract void stepOut() throws IOException;
    public abstract void stepOver() throws IOException;

    public abstract void addBreakpoint(int lineNumber) throws IOException;
    public abstract int getLineNumber() throws IOException;

    public boolean submitCommand(DebuggerCommand command)
    throws InterruptedException
    {
        boolean result = false;
        synchronized (commandLock)
        {
            if (command == null)
            {
                this.command = command;
                result = true;
            }
        }
        return result;
    }

    public boolean waiting()
    {
        boolean waiting;
        synchronized (commandLock)
        {
            waiting = (command != null);
        }
        return waiting;
    }

    // TODO change to run
    void runWrapper(Wrapper wrapper)
    {
        
    }
}

// TODO the run debugger functionality which takes a Wrapper and launches the timer,
// handles the receive commands, etc. This should be a static function of Wrapper.

