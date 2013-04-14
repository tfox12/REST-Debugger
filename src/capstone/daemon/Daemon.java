package capstone.daemon;

import capstone.wrapper.*;

public class Daemon
{
    public static void main(String... args)
    throws Exception // FIXME
    {
        String programText = "#include <iostream>\n"
                           + "using namespace std;\n"
                           + "int main() {\n"
                           + "    cout << \"Hello, there!\\n\";\n"
                           + "}\n";

        GdbWrapper wrapper = new GdbWrapper(1342, 0);
        wrapper.prepare(programText);
        wrapper.runProgram();
        //wrapper.runProgram();
        String output = wrapper.getStdOut();

        System.out.println("Read output: " + output);
        String expectedOutput = "Hello, there!\n";
        if (!expectedOutput.equals(output)) {
            System.out.println("Error!");
        }
    }
}

