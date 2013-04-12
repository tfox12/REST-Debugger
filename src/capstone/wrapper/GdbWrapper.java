package capstone.wrapper;

import capstone.util.*;
import java.util.*;
import java.lang.*;
import java.io.*;

public class GdbWrapper
{
    public GdbWrapper(int userId, int debuggerId)
    {
        // TODO generate filenames
    }

    private String programTextFilename;
    private String programBinaryFilename;

    private String fromProgramFilename;
    private String toProgramFilename;

    private Process debuggerProcess;
    private PrintStream toGdb;
    private Scanner fromGdb;
    private PrintStream toProgram;
    private BufferedInputStream fromProgram;

    List<ProgramError> prepare(String programText)
    throws IOException, InterruptedException
    {
        FileWriter fileOut = new FileWriter(programTextFilename);
        fileOut.write(programText);
        fileOut.close();

        String command = "g++ -g " + programTextFilename + " -o " + programBinaryFilename;
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);
        Process compileProcess = builder.start();
        Scanner compilerOutput = new Scanner(compileProcess.getInputStream());
        int result = compileProcess.waitFor();

        if (result != 0)
        {
            ArrayList<ProgramError> errors = new ArrayList<ProgramError>();

            // TODO finish this after we have tested the rest

            return errors;
        }

        createGdbProcess();

        String startCommand = "start 0 > " + toProgramFilename + " 1 > " + fromProgramFilename + " 2 > " + fromProgramFilename;
        toGdb.println(startCommand);
        // TODO open those files as stuffs

        return new ArrayList<ProgramError>();
    }

    // TODO kill debugger

    public void runProgram()
    throws IOException
    {
        String command = "c";
        toGdb.println(command);

        String output = readUntilPrompt();
    }

    public String getStdOut()
    {
        if (fromProgram.available() == 0)
        {
            return "";
        }
        else
        {
            String output;
            // TODO read it
            return output;
        }
    }

    // TODO other calls

    private void createGdbProcess()
    throws IOException
    {
        String command = "gdb -q " + programBinaryFilename;
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true); // merges stdout, stderr
        debuggerProcess = builder.start();
        fromGdb = new Scanner(debuggerProcess.getInputStream());
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
        // TODO implement this
        return "";
    }
}

