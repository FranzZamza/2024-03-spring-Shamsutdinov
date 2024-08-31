package ru.otus.hw.config;


import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class AppConfig {

    private static final int THREAD_POOL_SIZE = 2;

    @Bean(destroyMethod = "close")
    public NioEventLoopGroup nioEventLoopGroup() {
        return new NioEventLoopGroup(THREAD_POOL_SIZE);
    }

    @Bean
    public ReactiveWebServerFactory reactiveWebServerFactory(NioEventLoopGroup eventLoopGroup) {
        var factory = new NettyReactiveWebServerFactory();
        factory.addServerCustomizers(builder -> builder.runOn(eventLoopGroup));
        return factory;
    }

    @Bean
    public Scheduler workerPool() {
        return Schedulers.newParallel("worker-thread", THREAD_POOL_SIZE);
    }
}
