package capstone.wrapper;

import capstone.util.*;
import java.util.*;
import java.io.*;

public class GdbWrapper
{
    public GdbWrapper(int userId, int debuggerId)
    {
        
    }

    private String programTextFilename;
    private String programBinaryFilename;

    private Process debuggerProcess;
    private OutputStream toGdb;
    private InputStream fromGdb;
    private OutputStream toProgram;
    private InputStream fromProgram;

    // TODO prepare
    // TODO kill debugger
    // TODO run program

    private void createGdbProcess(String binaryFilename)
    throws IOException
    {
        String command = "gdb -q " + binaryFilename;
        // TODO change to process builder so we can merge stdout, stderr
        // see: http://docs.oracle.com/javase/6/docs/api/java/lang/ProcessBuilder.html
        debuggerProcess = Runtime.getRuntime().exec(command);
        fromGdb = debuggerProcess.getInputStream(); // TODO buffer this
        toGdb = debuggerProcess.getOutputStream(); // TODO Buffer this
        // TODO merge stdout, stderr
    }

    private void write(String command)
    throws IOException
    {
        toGdb.write(command.getBytes());
    }

    private String readUntilPrompt()
    throws IOException
    {
        // TODO
        return "";
    }
}

