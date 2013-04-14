package capstone.wrapper;

import capstone.util.*;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public abstract class Wrapper extends Thread
{
    private Object requestLock;
    private DebuggerRequest request;

    public Wrapper()
    {
        requestLock = new Object();
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

    public boolean submitRequest(DebuggerRequest request)
    throws InterruptedException
    {
        boolean result = false;
        synchronized (requestLock)
        {
            if (request == null)
            {
                this.request = request;
                result = true;
            }
        }
        return result;
    }

    public boolean waiting()
    {
        boolean waiting;
        synchronized (requestLock)
        {
            waiting = (request != null);
        }
        return waiting;
    }

    private DebuggerRequest getRequest()
    {
        synchronized (requestLock)
        {
            return request;
        }
    }

    private void clearRequest()
    {
        synchronized (requestLock)
        {
            request.monitor.notifyAll();
            request = null;
        }
    }

    void run(Wrapper wrapper)
    {
        
    }
}

// TODO the run debugger functionality which takes a Wrapper and launches the timer,
// handles the receive commands, etc. This should be a static function of Wrapper.

