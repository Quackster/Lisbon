package net.h4bbo.lisbon.messages.incoming.handshake;

import net.h4bbo.lisbon.crypto.HabboCipher;
import net.h4bbo.lisbon.crypto.SecretKeyCodec;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.handshake.END_OF_CRYPTO_PARAMS;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.GameChannelPipeline;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public class SECRETKEY implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) {
        String encodedSecretKey = reader.readString();
        int secretKey = SecretKeyCodec.secretDecode(encodedSecretKey);

        if (player.getCryptoMode() == Player.CryptoMode.NONE) {
            player.getLogger().warn("Ignoring SECRETKEY before DH setup");
            return;
        }

        HabboCipher cipher = new HabboCipher();
        cipher.initServerToClientSecretKey(secretKey);
        GameChannelPipeline.enableOutboundCrypto(player, cipher);
        player.send(new END_OF_CRYPTO_PARAMS());
    }
}
