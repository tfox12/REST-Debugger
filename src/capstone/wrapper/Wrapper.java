package capstone.wrapper;

import capstone.util.*;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public abstract class Wrapper extends Thread
{
    private Object requestLock;
    private DebuggerRequest request;
    private boolean active;

    public Wrapper()
    {
        requestLock = new Object();
        active = true;
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
        synchronized (requestLock)
        {
            if (request == null)
            {
                this.request = request;
                requestLock.notify();
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public boolean waiting()
    {
        synchronized (requestLock)
        {
            return (request != null);
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

    public void shutdown()
    {
        active = false;
    }

    @Override
    public void run()
    {

        activeLoop: while (active)
        {
            synchronized (requestLock)
            {
                while (request == null)
                {
                    try
                    {
                        requestLock.wait();
                    }
                    catch (InterruptedException exception)
                    {
                        if (active)
                        {
                            continue;
                        }
                        else
                        {
                            // To whoever reads this: I'm sorry.
                            // I decided to use a label and I
                            // should be punished.  --ntietz
                            break activeLoop;
                        }
                    }
                }

                // TODO handle the request here
            }

            clearRequest();
        } 
    }
}

// TODO the run debugger functionality which takes a Wrapper and launches the timer,
// handles the receive commands, etc. This should be a static function of Wrapper.

