package capstone.wrapper;

import capstone.util.*;
import java.util.*;
import java.lang.*;
import java.io.*;

public class GdbWrapper extends Wrapper
{
    public GdbWrapper(int userId, int debuggerId)
    {
        programTextFilename = "/tmp/program_" + userId + "_" + debuggerId + ".cpp";
        programBinaryFilename = "/tmp/program_" + userId + "_" + debuggerId + ".out";

        fromProgramFilename = "/tmp/fromprog" + userId + "_" + debuggerId;
        toProgramFilename = "/tmp/toprog" + userId + "_" + debuggerId;
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

    public List<ProgramError> prepare(String programText)
    throws IOException, InterruptedException
    {
        FileWriter fileOut = new FileWriter(programTextFilename);
        fileOut.write(programText);
        fileOut.close();

        //String command = "g++ -g " + programTextFilename + " -o " + programBinaryFilename;
        ProcessBuilder builder = new ProcessBuilder("g++", "-g", programTextFilename, "-o", programBinaryFilename);
        builder.redirectErrorStream(true);
        Process compileProcess = builder.start();
        Scanner compilerOutput = new Scanner(compileProcess.getInputStream());
        int result = compileProcess.waitFor();

        String[] mkfifo1 = {"touch", fromProgramFilename}; // FIXME this is a stupid variable name
        String[] mkfifo2 = {"touch", toProgramFilename}; // FIXME this is a stupid variable name
        Runtime.getRuntime().exec(mkfifo1).waitFor();
        Runtime.getRuntime().exec(mkfifo2).waitFor();

        if (result != 0)
        {
            ArrayList<ProgramError> errors = new ArrayList<ProgramError>();

            // TODO finish this after we have tested the rest
            System.out.println("FOUND AN ERROR!");

            return errors;
        }

        createGdbProcess();

        readUntilPrompt();
        String startCommand = "start 0 > " + toProgramFilename + " 1 > " + fromProgramFilename + " 2 > " + fromProgramFilename;
        write(startCommand);
        readUntilPrompt();

        return new ArrayList<ProgramError>();
    }

    public void killDebugger()
    {
        debuggerProcess.destroy();
    }

    public void runProgram()
    throws IOException
    {
        write("c");
        String output = readUntilPrompt();
    }

    public String getStdOut()
    throws IOException
    {
        int numAvailable = fromProgram.available();
        if (numAvailable == 0)
        {
            return "";
        }
        else
        {
            byte[] bytes = new byte[numAvailable];
            fromProgram.read(bytes, 0, numAvailable);
            String output = new String(bytes);
            return output;
        }
    }

    public void provideInput(String input)
    {
        toProgram.print(input);
        toProgram.flush();
        // TODO consider if we should make this print, or println?
    }

    public StackFrame getLocalValues()
    {
        // TODO implement this
        return null;
    }

    public List<StackFrame> getStack()
    {
        // TODO implement this
        return null;
    }

    public String evaluateExpression(String expression)
    throws IOException
    {
        write("print " + expression);
        String output = readUntilPrompt();

        int equalsIndex = output.indexOf('=');
        if (equalsIndex == -1)
        {
            return "error: expression not valid";
        }
        else
        {
            output.substring(equalsIndex + 2, output.length() - 1);
        }

        return null;
    }

    public void stepIn()
    throws IOException
    {
        write("step");
        readUntilPrompt();
    }

    public void stepOut()
    throws IOException
    {
        write("finish");
        String output = readUntilPrompt();
        if (output.equals("\"finish\" not meaningful in the outermost frame.\n"))
            // TODO test this behavior
        {
            runProgram();
        }
    }

    public void stepOver()
    throws IOException
    {
        write("next");
        readUntilPrompt();
    }

    public void addBreakpoint(int lineNumber)
    throws IOException
    {
        write("break " + lineNumber);
        readUntilPrompt();
    }

    public int getLineNumber()
    throws IOException
    {
        int lineNumber = 0;

        write("info line");
        String output = readUntilPrompt();

        Scanner outputScanner = new Scanner(output);
        outputScanner.next();

        return outputScanner.nextInt();
    }

    private void createGdbProcess()
    throws IOException
    {
        //String command = "gdb -q " + programBinaryFilename;
        ProcessBuilder builder = new ProcessBuilder("gdb", "-q", programBinaryFilename);
        builder.redirectErrorStream(true); // merges stdout, stderr
        debuggerProcess = builder.start();
        fromGdb = new Scanner(new BufferedInputStream(debuggerProcess.getInputStream()));
        toGdb = new PrintStream(debuggerProcess.getOutputStream());

        fromProgram = new BufferedInputStream(new FileInputStream(fromProgramFilename));
        toProgram = new PrintStream(new FileOutputStream(toProgramFilename));
    }

    private void write(String command)
    throws IOException
    {
        toGdb.println(command);
        toGdb.flush();
    }

    private String readUntilPrompt()
    throws IOException
    {
        fromGdb.useDelimiter("\\(gdb\\) ");
        String line = fromGdb.next();
        return line;
    }
}

