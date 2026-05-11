package net.h4bbo.lisbon.server.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.h4bbo.lisbon.crypto.HabboCipher;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.server.netty.GameChannelPipeline;

/**
 * Encrypts outbound frames after the normal Lisbon encoder serializes them.
 */
public class OutboundCryptoEncoder extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
        Player player = ctx.channel().attr(Player.PLAYER_KEY).get();
        if (player == null || !player.isOutboundEncrypted()) {
            out.writeBytes(msg, msg.readerIndex(), msg.readableBytes());
            return;
        }

        HabboCipher cipher = player.getOutboundCipher();
        if (cipher == null) {
            GameChannelPipeline.disableOutboundCrypto(player);
            out.writeBytes(msg, msg.readerIndex(), msg.readableBytes());
            return;
        }

        byte[] plaintext = new byte[msg.readableBytes()];
        msg.getBytes(msg.readerIndex(), plaintext);
        out.writeBytes(cipher.encryptToHex(plaintext));
    }
}
