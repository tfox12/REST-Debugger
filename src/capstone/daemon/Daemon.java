package capstone.daemon;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class Daemon
{
    public Daemon(int port)
    {
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new DaemonPipelineFactory());

        bootstrap.bind(new InetSocketAddress(port));
    }

    public static void main(String... args)
    throws Exception // FIXME
    {
        new Daemon(6789);
    }
}

