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
        pipeline.addLast("aggrigate",   new HttpObjectAggregator(1048567));
        pipeline.addLast("encoder",     new HttpRequestEncoder());
        pipeline.addLast("handler",     new DaemonHandler());
    }
}
