package capstone.daemon;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Daemon
{
    public Daemon(int port) throws InterruptedException
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new DaemonInitializer());

            Channel ch = bootstrap.bind(port).sync().channel();
            ch.closeFuture().sync();
        }
        finally
        {
            bossGroup.shutdown();
            workerGroup.shutdown();
        }
        ServerBootstrap bootstrap = new ServerBootstrap();

    }

    public static void main(String... args)
    throws InterruptedException
    {
        new Daemon(6789); // FIXME change to command line arg
    }
}

