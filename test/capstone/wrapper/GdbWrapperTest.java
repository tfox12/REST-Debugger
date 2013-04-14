package capstone.wrapper;

import static org.junit.Assert.*;
import org.junit.Test;

public class GdbWrapperTest
{
    @Test
    public void test()
    throws Exception
    {
        String programText = "#include <iostream>\n"
                           + "using namespace std;\n"
                           + "int main() {\n"
                           + "    cout << \"Hello, there!\\n\";\n"
                           + "}\n";

        GdbWrapper wrapper = new GdbWrapper(1342, 0);
        wrapper.prepare(programText);
        wrapper.runProgram();
        String output = wrapper.getStdOut();

        String expectedOutput = "Hello, there!\n";
        assertEquals(expectedOutput, output);
    }
}

