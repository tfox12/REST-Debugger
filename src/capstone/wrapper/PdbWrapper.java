package capstone.wrapper;

import capstone.util.*;
import java.util.*;
import java.io.*;

public class PdbWrapper extends Wrapper
{
    public PdbWrapper(int userId, int debuggerId) 
    {
        super();
        
        programTextFilename = "/tmp/program_" + userId + "_" + 
                debuggerId + ".py";
        fromProgramFilename = "/tmp/fromprog" + userId + "_" + debuggerId;
        toProgramFilename   = "/tmp/toprog"   + userId + "_" + debuggerId;
        
    }
    
    private String programTextFilename;

    private String fromProgramFilename;
    private String toProgramFilename;

    private Process debuggerProcess;
    private PrintStream toPdb;
    private Scanner fromPdb;
    private PrintStream toProgram;
    private BufferedInputStream fromProgram;
    
    @Override
    protected List<ProgramError> prepare(String programText) 
    throws IOException, InterruptedException
    {
        ArrayList<ProgramError> errors = new ArrayList<ProgramError>();
        return errors;
    }
    
    @Override
    protected void killDebugger()
    {
        if (debuggerProcess != null) 
        {
            debuggerProcess.destroy();
        }
    }
    
    @Override
    protected void cleanup()
    {
        try 
        {
            if (toPdb != null) 
            {
                toPdb.close();
            }
            if (fromPdb != null) 
            {
                fromPdb.close();
            }
            if (toProgram != null) 
            {
                toProgram.close();
            }
            if (fromProgram != null)
            {
                fromProgram.close();
            }
            
            safeDelete(programTextFilename);
            safeDelete(fromProgramFilename);
            safeDelete(toProgramFilename);
        }
        catch (Exception exception) 
        {
            exception.printStackTrace();
        }
    }
    
    @Override
    protected void runProgram()
    throws IOException
    {
        write("c");
        String output = readUntilPrompt();
    }
    
    @Override
    protected String getStdOut() 
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
    
    @Override
    public void provideInput(String input) 
    {
        toProgram.print(input);
        toProgram.flush();
    }
    
    @Override
    protected StackFrame getLocalValues() 
    {
        ArrayList<ProgramVariable> vars = new ArrayList<ProgramVariable>();
        
        //TODO: convert all that C++ code into Java and ram it in down here
        
        StackFrame localFrame = new StackFrame("test", 5, vars);
        return localFrame;
    }
    
    @Override
    protected List<StackFrame> getStack() throws IOException 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    @Override
    protected String evaluateExpression(String expression)
    throws IOException
    {
        write("p " + expression + "\n");
        String output = readUntilPrompt();
        
        //TODO: Make sure the expression is valid
        if (false) 
        {
            return "Error: Expression not valid";
        }
        else 
        {
            return output;
        }
    }
    
    @Override
    protected void stepIn()
    throws IOException
    {
        write("step");
        readUntilPrompt();
    }
    
    @Override
    protected void stepOut()
    throws IOException
    {
        write("return");
        readUntilPrompt();
    }
    
    @Override
    protected void stepOver()
    throws IOException
    {
        write("next");
        readUntilPrompt();
    }
    
    @Override
    protected void addBreakpoint(int lineNumber)
    throws IOException
    {
        write("break " + lineNumber);
        readUntilPrompt();
    }
    
    @Override
    protected int getLineNumber()
    throws IOException
    {
        write("info line");
        String output = readUntilPrompt();

        Scanner outputScanner = new Scanner(output);
        outputScanner.next();

        int lineNumber = outputScanner.nextInt();
        return lineNumber;
    }
    
    private void createPdbProcess()
    throws IOException
    {
        ProcessBuilder builder = new ProcessBuilder();
        builder.redirectErrorStream(true);
        debuggerProcess = builder.start();
        fromPdb = new Scanner(new BufferedInputStream(debuggerProcess.getInputStream()));
        toPdb   = new PrintStream(debuggerProcess.getOutputStream());
    }
    
    private void write(String command) 
    throws IOException
    {
        toPdb.println(command);
        toPdb.flush();
    }
    
    private String readUntilPrompt() {
        fromPdb.useDelimiter("\\(pdb\\) ");
        String line = fromPdb.next();
        return line;
    }
    
    private void safeDelete(String filename) {
        if (filename != null) 
        {
            try
            {
                (new File(filename)).delete();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
