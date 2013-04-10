package capstone.wrapper;

import capstone.util.*;
import java.util.*;

public interface Wrapper
{
    public List<ProgramError> prepare(String programText);
}

