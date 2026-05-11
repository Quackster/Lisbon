package net.h4bbo.lisbon.server.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.h4bbo.lisbon.crypto.HabboCipher;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.server.netty.GameChannelPipeline;
import net.h4bbo.lisbon.util.encoding.Base64Encoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Decrypts Director-style init handshake frames before the normal packet
 * decoder sees them.
 */
public class InboundCryptoDecoder extends ByteToMessageDecoder {
    private static final Logger log = LoggerFactory.getLogger(InboundCryptoDecoder.class);
    private static final int ENCRYPTED_LENGTH_HEX_BYTES = 6;
    private static final int PLAINTEXT_LENGTH_BYTES = 3;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        Player player = ctx.channel().attr(Player.PLAYER_KEY).get();
        if (player == null) {
            removeSelf(ctx);
            passThrough(in, out);
            return;
        }

        HabboCipher cipher = player.getInboundCipher();
        if (!player.isInboundEncrypted() || cipher == null) {
            fallbackToPlaintext(player, in, out, null);
            return;
        }

        while (true) {
            if (in.readableBytes() < ENCRYPTED_LENGTH_HEX_BYTES) {
                if (in.readableBytes() >= PLAINTEXT_LENGTH_BYTES && !looksLikeHex(in, in.readableBytes())) {
                    fallbackToPlaintext(player, in, out, "Session {} stayed in plaintext after crypto setup, falling back to plaintext decoding");
                }
                return;
            }

            if (!looksLikeHex(in, ENCRYPTED_LENGTH_HEX_BYTES)) {
                fallbackToPlaintext(player, in, out, "Session {} sent non-hex data after crypto setup, falling back to plaintext decoding");
                return;
            }

            in.markReaderIndex();

            byte[] encryptedLength = new byte[ENCRYPTED_LENGTH_HEX_BYTES];
            in.readBytes(encryptedLength);

            byte[] decryptedLength = cipher.copy().decryptHexStream(encryptedLength);
            if (!isHabboBase64Prefix(decryptedLength)) {
                log.warn("Invalid encrypted length prefix for {}", remoteAddress(player));
                return;
            }

            int bodyLength = Base64Encoding.decode(new byte[]{decryptedLength[0], decryptedLength[1], decryptedLength[2]});
            if (bodyLength < 2) {
                log.warn("Invalid encrypted message length {} for {}", bodyLength, remoteAddress(player));
                return;
            }

            int encryptedBodyHexBytes = bodyLength * 2;
            if (in.readableBytes() < encryptedBodyHexBytes) {
                in.resetReaderIndex();
                return;
            }

            byte[] encryptedFrame = new byte[ENCRYPTED_LENGTH_HEX_BYTES + encryptedBodyHexBytes];
            System.arraycopy(encryptedLength, 0, encryptedFrame, 0, encryptedLength.length);
            in.readBytes(encryptedFrame, encryptedLength.length, encryptedBodyHexBytes);

            out.add(Unpooled.wrappedBuffer(cipher.decryptFrame(encryptedFrame)));
        }
    }

    private void removeSelf(ChannelHandlerContext ctx) {
        if (ctx.pipeline().context(this) != null) {
            ctx.pipeline().remove(this);
        }
    }

    private void fallbackToPlaintext(Player player, ByteBuf in, List<Object> out, String reason) {
        GameChannelPipeline.disableInboundCrypto(player);
        if (reason != null) {
            log.warn(reason, remoteAddress(player));
        }
        passThrough(in, out);
    }

    private void passThrough(ByteBuf in, List<Object> out) {
        if (in.isReadable()) {
            out.add(in.readRetainedSlice(in.readableBytes()));
        }
    }

    private static boolean looksLikeHex(ByteBuf in, int bytesToCheck) {
        int readerIndex = in.readerIndex();
        for (int i = 0; i < bytesToCheck; i++) {
            byte value = in.getByte(readerIndex + i);
            if (!isHex(value)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isHex(byte value) {
        return (value >= '0' && value <= '9')
                || (value >= 'A' && value <= 'F')
                || (value >= 'a' && value <= 'f');
    }

    private static boolean isHabboBase64Prefix(byte[] value) {
        return value.length >= PLAINTEXT_LENGTH_BYTES
                && isHabboBase64Byte(value[0])
                && isHabboBase64Byte(value[1])
                && isHabboBase64Byte(value[2]);
    }

    private static boolean isHabboBase64Byte(byte value) {
        int unsigned = value & 0xFF;
        return unsigned >= 64 && unsigned <= 127;
    }

    private static String remoteAddress(Player player) {
        return player.getNetwork().getChannel().remoteAddress() == null
                ? "unknown"
                : player.getNetwork().getChannel().remoteAddress().toString();
    }
}
