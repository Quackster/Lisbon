package org.alexdev.http.controllers.habblet;

import net.h4bbo.lisbon.dao.mysql.BadgeDao;
import net.h4bbo.lisbon.dao.mysql.GroupDao;
import net.h4bbo.lisbon.dao.mysql.MessengerDao;
import net.h4bbo.lisbon.dao.mysql.PlayerDao;
import net.h4bbo.lisbon.game.badges.Badge;
import net.h4bbo.lisbon.game.messenger.Messenger;
import net.h4bbo.lisbon.game.messenger.MessengerManager;
import net.h4bbo.lisbon.game.player.PlayerDetails;
import net.h4bbo.lisbon.server.rcon.messages.RconHeader;
import net.h4bbo.lisbon.util.StringUtil;
import net.h4bbo.lisbon.util.config.GameConfiguration;
import org.alexdev.duckhttpd.response.ResponseBuilder;
import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.template.Template;
import org.alexdev.http.dao.CommunityDao;
import org.alexdev.http.util.RconUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class XmlController {
    public static void promoHabbos(WebConnection webConnection) throws ParserConfigurationException, TransformerException {
        var habbos = PlayerDao.getRecentHabbos(10);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        // Root <habbos>
        Element root = doc.createElement("habbos");
        doc.appendChild(root);

        for (var h : habbos) {
            Element habbo = doc.createElement("habbo");
            habbo.setAttribute("id", String.valueOf(h.getId()));
            habbo.setAttribute("name", h.getName());
            habbo.setAttribute("motto", h.getMotto());
            habbo.setAttribute("url", "/home/" + h.getName());
            habbo.setAttribute("image", GameConfiguration.getInstance().getString("site.path") + "/habbo-imaging/avatarimage?figure=" + h.getFigure() + "&size=b&direction=4&head_direction=5&crr=0&gesture=&frame=1");

            String badgeUrl = "";
            var badges = BadgeDao.getBadges(h.getId());

            if (!badges.isEmpty()) {
                badges.sort(Comparator.comparingInt(Badge::getSlotId));
                badgeUrl = GameConfiguration.getInstance().getString("static.content.path") + "/c_images/Badges/" + badges.get(0).getBadgeCode() + ".gif";
            }

            habbo.setAttribute("badge", badgeUrl);
            habbo.setAttribute("status", h.isOnline() ? "1" : "0");

            /*
            if (h.groupBadgeUrl != null && !h.groupBadgeUrl.isEmpty()) {
                habbo.setAttribute("groupBadge", h.groupBadgeUrl);
            }*/

            String groupBadge = "";

            if (h.getFavouriteGroupId() > 0) {
                var group = GroupDao.getGroup(h.getFavouriteGroupId());

                if (group != null) {
                    groupBadge = GameConfiguration.getInstance().getString("site.path") + "/habbo-imaging/badge/" + group.getBadge() + ".gif";
                }
            }

            habbo.setAttribute("groupBadge", groupBadge);
            root.appendChild(habbo);
        }

        // Convert Document -> String
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

        StreamResult result = new StreamResult(new java.io.StringWriter());
        transformer.transform(new DOMSource(doc), result);

        String xmlResponse = result.getWriter().toString();
        webConnection.send(ResponseBuilder.create("text/xml", xmlResponse));
    }

    public static void promoHabbosV2(WebConnection webConnection) {


    }
}
