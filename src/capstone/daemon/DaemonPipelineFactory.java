package capstone.daemon;

import static org.jboss.netty.channel.Channels.*;

import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;

public class DaemonPipelineFactory implements ChannelPipelineFactory
{
    public ChannelPipeline getPipeline()
    {
        ChannelPipeline pipeline = pipeline();
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggrigate", new HttpChunkAggregator(1048567));
        pipeline.addLast("encoder", new HttpRequestEncoder());
        pipeline.addLast("handler", new DaemonHandler());
        return pipeline;
    }
}
