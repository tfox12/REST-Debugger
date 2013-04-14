package capstone.daemon;

import java.util.HashMap;
import java.net.URLDecoder;

import io.netty.util.CharsetUtil;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

public class DaemonHandler extends ChannelInboundMessageHandlerAdapter<DefaultFullHttpRequest>
{
    @Override
    public void messageReceived(
                      ChannelHandlerContext ctx
                    , DefaultFullHttpRequest request)
    {
        String body = request.data().toString(CharsetUtil.UTF_8);
        
        try 
        { 
            HashMap<String, String> args = parseBody(body);
            
        }
        catch(Exception e) 
        {
            System.err.println("we are not in buisness");
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
