package capstone.daemon;

import org.jboss.netty.channel.*;

public class DaemonHandler extends SimpleChannelUpstreamHandler
{
    @Override
    public void messageReceived(
                      ChannelHandlerContext ctx
                    , MessageEvent e)
    {
    }

    @Override
    public void exceptionCaught(
                      ChannelHandlerContext ctx
                    , ExceptionEvent e)
    {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
