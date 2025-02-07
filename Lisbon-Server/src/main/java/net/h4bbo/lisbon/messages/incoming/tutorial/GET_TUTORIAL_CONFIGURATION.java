package net.h4bbo.lisbon.messages.incoming.tutorial;

import net.h4bbo.lisbon.game.player.Player;
import net.h4bbo.lisbon.game.tutorial.TutorialTopic;
import net.h4bbo.lisbon.messages.outgoing.tutorial.TUTORIAL_CONFIGURATION;
import net.h4bbo.lisbon.messages.types.MessageEvent;
import net.h4bbo.lisbon.server.netty.streams.NettyRequest;

import java.util.ArrayList;
import java.util.List;

public class GET_TUTORIAL_CONFIGURATION implements MessageEvent {

    @Override
    public void handle(Player player, NettyRequest reader) {
        TutorialTopic topic = new TutorialTopic();
        topic.setId(1);
        topic.setName("own_user");
        topic.setStatus(0);

        List<TutorialTopic> topics = new ArrayList<>();
        topics.add(topic);

        player.send(new TUTORIAL_CONFIGURATION(1, "ur mom", topics));
    }
}