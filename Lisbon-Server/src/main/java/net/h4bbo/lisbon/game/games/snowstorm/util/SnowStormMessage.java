package net.h4bbo.lisbon.game.games.snowstorm.util;

import net.h4bbo.lisbon.game.games.player.GamePlayer;
import net.h4bbo.lisbon.game.games.snowstorm.SnowStormGame;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

public interface SnowStormMessage {
    void handle(NettyRequest request, SnowStormGame snowStormGame, GamePlayer gamePlayer) throws Exception;
}
