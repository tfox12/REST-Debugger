package capstone.wrapper;

import capstone.util.*;

import static org.junit.Assert.*;
import org.junit.*;

import java.util.*;

public class GdbWrapperTest
{
    private static String helloWorldProgram;
    private static String longerMainProgram;
    private static String badCompileProgram;
    private static String needsInputProgram;
    private static String functionalProgram;

    @BeforeClass
    public static void setupPrograms()
    {
        // The "hello world" program tests basic functionality.
        helloWorldProgram = "#include <iostream>\n"
                          + "\n"
                          + "using namespace std;\n"
                          + "\n"
                          + "int main() {\n"
                          + "    cout << \"Hello, world!\" << endl;\n"
                          + "    return 0;\n"
                          + "}\n";

        // The "longer main" program should be useful for testing some stepping.
        longerMainProgram = "#include <iostream>\n"
                          + "\n"
                          + "using namespace std;\n"
                          + "\n"
                          + "int main() {\n"
                          + "    int x = 10;\n"
                          + "    int y = 13;\n"
                          + "    float pi = 3.14159f;\n"
                          + "    cout << \"x is: \" << x << endl;\n"
                          + "    cout << \"y is: \" << y << endl;\n"
                          + "    cout << pi << \" is delicious\\n\";\n"
                          + "    return 0;\n"
                          + "}\n";

        // The "bad compile" program should give 2 compile errors.
        badCompileProgram = "#include <iostream>\n"
                          + "\n"
                          + "using namespace std;\n"
                          + "\n"
                          + "int main() {\n"
                          + "    int  = 10;\n"
                          + "    int y = 13;\n"
                          + "    float pi = 3.14159f\n"
                          + "    cout << \"x is: \" << x << endl;\n"
                          + "    cout << \"y is: \" << y << endl;\n"
                          + "    cout << pi << \" is delicious\\n\";\n"
                          + "    return 0;\n"
                          + "}\n";

        // The "needs input" program tests providing input functionality.
        needsInputProgram = "#include <iostream>\n"
                          + "\n"
                          + "using namespace std;\n"
                          + "\n"
                          + "int main() {\n"
                          + "    int x;\n"
                          + "    cin >> x;\n"
                          + "    cout << x << \"\\n\";\n"
                          + "    return 0;\n"
                          + "}\n";

        // The "functional" program is useful for testing the get local variables
        // functionality, evaluate expression, and stack trace retrieval.
        functionalProgram = "#include <iostream>\n"
                          + "using namespace std;\n"
                          + "\n"
                          + "int factorial(int n) {\n"
                          + "    if (n == 0)\n"
                          + "        return 1;\n"
                          + "    return n * factorial(n-1);\n"
                          + "}\n"
                          + "\n"
                          + "int main() {\n"
                          + "    int k;\n"
                          + "    cin >> k;\n"
                          + "    int result = factorial(k);\n"
                          + "    cout << result << endl;\n"
                          + "}\n";
    }

    @Test
    public void helloWorldTest()
    throws Exception
    {
        Wrapper wrapper = new GdbWrapper(1342, 0);

        List<ProgramError> errors = wrapper.prepare(helloWorldProgram);
        assertEquals("program should compile", 0, errors.size());
        assertEquals("incorrect line number", 6, wrapper.getLineNumber());

        wrapper.runProgram();
        wrapper.killDebugger();
        String output = wrapper.getStdOut();

        String expectedOutput = "Hello, world!\n";
        assertEquals("stdout did not match", expectedOutput, output);
    }
}

