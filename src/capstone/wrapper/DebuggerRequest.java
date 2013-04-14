package capstone.wrapper;

public class DebuggerRequest
{
    /**
     * command is what we want the Wrapper object to do.
     */
    public DebuggerCommand command;

    /**
     * data is the submitted data for performing the request.
     */
    public String data;

    /**
     * result is the result after the request is done.
     */
    public String result;

    /**
     * This is the monitor for the requesting thread to wait on.
     */
    public Object monitor;

    public DebuggerRequest(DebuggerCommand command, String data, String result)
    {
        this.command = command;
        this.data = data;
        this.result = result;
        monitor = new Object();
    }
}

