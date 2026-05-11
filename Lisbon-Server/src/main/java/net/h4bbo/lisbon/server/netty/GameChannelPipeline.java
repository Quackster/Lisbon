package net.h4bbo.lisbon.server.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import net.h4bbo.lisbon.crypto.HabboCipher;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.server.netty.codec.InboundCryptoDecoder;
import net.h4bbo.lisbon.server.netty.codec.NetworkDecoder;
import net.h4bbo.lisbon.server.netty.codec.NetworkEncoder;
import net.h4bbo.lisbon.server.netty.codec.OutboundCryptoEncoder;

public final class GameChannelPipeline {
    public static final String INBOUND_CRYPTO = "inboundCrypto";
    public static final String DECODER = "gameDecoder";
    public static final String OUTBOUND_CRYPTO = "outboundCrypto";
    public static final String ENCODER = "gameEncoder";

    private GameChannelPipeline() {
    }

    public static void resetCrypto(Player player) {
        ChannelPipeline pipeline = player.getNetwork().getChannel().pipeline();
        removeIfPresent(pipeline, INBOUND_CRYPTO);
        removeIfPresent(pipeline, OUTBOUND_CRYPTO);
        player.resetCrypto();
    }

    public static void enableInboundCrypto(Player player, HabboCipher cipher) {
        player.setInboundCipher(cipher);
        player.setInboundEncrypted(true);
        addBefore(player.getNetwork().getChannel().pipeline(), DECODER, NetworkDecoder.class, INBOUND_CRYPTO, new InboundCryptoDecoder());
    }

    public static void disableInboundCrypto(Player player) {
        removeIfPresent(player.getNetwork().getChannel().pipeline(), INBOUND_CRYPTO);
        player.setInboundCipher(null);
        player.setInboundEncrypted(false);
    }

    public static void enableOutboundCrypto(Player player, HabboCipher cipher) {
        player.setOutboundCipher(cipher);
        player.setOutboundEncrypted(true);
        addBefore(player.getNetwork().getChannel().pipeline(), ENCODER, NetworkEncoder.class, OUTBOUND_CRYPTO, new OutboundCryptoEncoder());
    }

    public static void disableOutboundCrypto(Player player) {
        removeIfPresent(player.getNetwork().getChannel().pipeline(), OUTBOUND_CRYPTO);
        player.setOutboundCipher(null);
        player.setOutboundEncrypted(false);
    }

    private static void addBefore(
            ChannelPipeline pipeline,
            String baseName,
            Class<? extends ChannelHandler> baseType,
            String handlerName,
            ChannelHandler handler
    ) {
        if (pipeline.get(handlerName) != null) {
            return;
        }

        if (pipeline.get(baseName) != null) {
            pipeline.addBefore(baseName, handlerName, handler);
            return;
        }

        ChannelHandlerContext baseContext = pipeline.context(baseType);
        if (baseContext != null) {
            pipeline.addBefore(baseContext.name(), handlerName, handler);
            return;
        }

        pipeline.addLast(handlerName, handler);
    }

    private static void removeIfPresent(ChannelPipeline pipeline, String handlerName) {
        if (pipeline.get(handlerName) != null) {
            pipeline.remove(handlerName);
        }
    }
}
