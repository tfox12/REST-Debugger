package capstone.wrapper;

import capstone.util.*;
import java.util.*;
import java.lang.*;
import java.io.*;

public class GdbWrapper
{
    public GdbWrapper(int userId, int debuggerId)
    {
        
    }

    private String programTextFilename;
    private String programBinaryFilename;

    private Process debuggerProcess;
    private PrintStream toGdb;
    private BufferedInputStream fromGdb;
    private PrintStream toProgram;
    private BufferedInputStream fromProgram;

    void prepare(String programText)
    {
        
    }

    // TODO kill debugger
    // TODO run program

    private void createGdbProcess(String binaryFilename)
    throws IOException
    {
        String command = "gdb -q " + binaryFilename;
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true); // merges stdout, stderr
        debuggerProcess = builder.start();
        fromGdb = new BufferedInputStream(debuggerProcess.getInputStream());
        toGdb = new PrintStream(debuggerProcess.getOutputStream());
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

