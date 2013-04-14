package capstone.daemon;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;

public class DaemonHandler extends ChannelInboundMessageHandlerAdapter<HttpRequest>
{
    @Override
    public void messageReceived(
                      ChannelHandlerContext ctx
                    , HttpRequest request)
    {
        System.out.println(request);
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
