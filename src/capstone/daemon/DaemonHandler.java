package capstone.daemon;

import capstone.wrapper.*;

import java.util.HashMap;
import java.net.URLDecoder;

import io.netty.util.CharsetUtil;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

public class DaemonHandler extends ChannelInboundMessageHandlerAdapter<DefaultFullHttpRequest>
{
    /**
     * Format for the key is: "userId_debuggerId"
     */
    static HashMap<String,Wrapper> wrapperMap = new HashMap<String,Wrapper>();

    @Override
    public void messageReceived(
                      ChannelHandlerContext ctx
                    , DefaultFullHttpRequest request)
    {
        String body = request.data().toString(CharsetUtil.UTF_8);
        
        try 
        { 
            HashMap<String, String> args = parseBody(body);

            // TODO add session tokens
            // TODO add specifier of C++ vs Python

            String userId = args.get("usrid");
            String debuggerId = args.get("dbgid");
            String wrapperKey = userId + "_" + debuggerId;

            String commandString = args.get("call");
            char commandChar = commandString.charAt(0); // TODO make sure it's len > 0
            DebuggerCommand command = DebuggerCommand.fromChar(commandChar);
            String data = args.get("data"); // TODO ensure that this is set
            DebuggerRequest debuggerRequest = new DebuggerRequest(command, data);

            // TODO determine the cases when we want to create a new one
            Wrapper wrapper = wrapperMap.get(wrapperKey);
            if (wrapper == null)
            {
                wrapper = new GdbWrapper(Integer.parseInt(userId), Integer.parseInt(debuggerId));
                wrapper.start();
                while (!wrapper.isActive());
                wrapperMap.put(wrapperKey, wrapper);
            }

            System.out.println("[daemon] Submitting a request...");
            wrapper.submitRequest(debuggerRequest);
            System.out.println("[daemon] Waiting on the monitor...");
            synchronized (debuggerRequest.monitor)
            {
                debuggerRequest.monitor.wait();
            }
            System.out.println("[daemon] Woke up!");
        }
        catch(Exception e) 
        {
            e.printStackTrace();
        }
    }

    private HashMap<String, String> parseBody(String body) throws Exception
    {
        HashMap<String, String> args = new HashMap<String, String>();
        String[] tokens = body.split("&");
        for(String token : tokens)
        {
            String[] kvp = token.split("=");
            args.put(URLDecoder.decode(kvp[0], "UTF-8")
                    ,URLDecoder.decode(kvp[1], "UTF-8"));
        }
        return args;
    }

    @Override
    public void exceptionCaught(
                      ChannelHandlerContext ctx
                    , Throwable cause)
    {
        cause.printStackTrace();
        ctx.close();
    }
}
