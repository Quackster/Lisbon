package net.h4bbo.lisbon.messages.incoming.handshake;

import net.h4bbo.lisbon.crypto.DiffieHellman;
import net.h4bbo.lisbon.crypto.HabboCipher;
import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.messages.outgoing.handshake.AVAILABLE_SETS;
import net.h4bbo.lisbon.messages.outgoing.handshake.SECRET_KEY;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.GameChannelPipeline;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;
import net.h4bbo.lisbon.util.config.GameConfiguration;

public class GENERATEKEY implements MessageEvent {
    private static final byte[] INIT_COMPAT_SHARED_SECRET = new byte[]{0x01};

    @Override
    public void handle(Player player, NettyRequest reader) {
        if (player.isLoggedIn()) {
            return;
        }

        reader.contents();
        player.setCryptoMode(Player.CryptoMode.INIT);

        HabboCipher cipher = new HabboCipher();
        cipher.initInitSocket(INIT_COMPAT_SHARED_SECRET);
        GameChannelPipeline.enableInboundCrypto(player, cipher);

        player.send(new SECRET_KEY(DiffieHellman.initCompatibilityPublicKeyHex()));

        //if (player.getVersion() <= 17) {
        player.send(new AVAILABLE_SETS("[" + GameConfiguration.getInstance().getString("users.figure.parts.default") + "]"));
        //}
    }
}
