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

    List<ProgramError> prepare(String programText)
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

        String startCommand = "start 0 > " + toProgramFilename + " 1 > " + fromProgramFilename + " 2 > " + fromProgramFilename;
        System.out.println(startCommand);
        toGdb.println(startCommand);
        //readUntilPrompt();
        // TODO open those files as stuffs

        return new ArrayList<ProgramError>();
    }

    // TODO kill debugger

    public void runProgram()
    throws IOException
    {
        String command = "c";
        toGdb.println(command);

        //String output = readUntilPrompt();
        while (fromGdb.hasNextByte())
        {
            System.out.println("LINE: " + fromGdb.nextLine());
        }
        //System.out.println("Run program output: " + output);
    }

    public String getStdOut()
    throws IOException
    {
        readUntilPrompt();
        int numAvailable = fromProgram.available();
        System.out.println("Available: " + numAvailable);
        if (numAvailable == 0)
        {
            return "";
        }
        else
        {
            String output = "";
            byte[] bytes = new byte[numAvailable];
            fromProgram.read(bytes, 0, numAvailable);
            return output;
        }
    }

    // TODO other calls

    private void createGdbProcess()
    throws IOException
    {
        //String command = "gdb -q " + programBinaryFilename;
        ProcessBuilder builder = new ProcessBuilder("gdb", "-q", programBinaryFilename);
        builder.redirectErrorStream(true); // merges stdout, stderr
        debuggerProcess = builder.start();
        fromGdb = new Scanner(debuggerProcess.getInputStream());
        toGdb = new PrintStream(debuggerProcess.getOutputStream());

        fromProgram = new BufferedInputStream(new FileInputStream(fromProgramFilename));
        toProgram = new PrintStream(new FileOutputStream(toProgramFilename));
    }

    private void write(String command)
    throws IOException
    {
        // TODO actually use this...
        // maybe change to a println?
        toGdb.write(command.getBytes());
    }

    private String readUntilPrompt()
    throws IOException, NoSuchElementException, IllegalStateException
    {
        // TODO implement this
        fromGdb.useDelimiter("\\(gdb\\) ");
        String line = fromGdb.next();
        System.out.println(line);

        return line;
    }
}

