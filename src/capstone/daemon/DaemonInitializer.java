package capstone.daemon;

import io.netty.channel.*;
import io.netty.channel.socket.*;
import io.netty.handler.codec.http.*;

public class DaemonInitializer extends ChannelInitializer<SocketChannel>
{
    public void initChannel(SocketChannel channel)
    {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("decoder",     new HttpRequestDecoder());
        pipeline.addLast("aggregate",   new HttpObjectAggregator(1048567));
        pipeline.addLast("encoder",     new HttpResponseEncoder());
        pipeline.addLast("handler",     new DaemonHandler());
    }
}
