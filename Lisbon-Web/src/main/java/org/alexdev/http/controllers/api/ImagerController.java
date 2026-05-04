package org.alexdev.http.controllers.api;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.h4bbo.avatara4j.badges.Badge;
import net.h4bbo.avatara4j.badges.BadgeSettings;
import net.h4bbo.avatara4j.badges.RenderType;
import net.h4bbo.avatara4j.figure.Avatar;
import net.h4bbo.avatara4j.figure.readers.FiguredataReader;
import net.h4bbo.avatara4j.figure.readers.LegacyFiguredataReader;
import net.h4bbo.avatara4j.figure.readers.ManifestReader;
import org.alexdev.duckhttpd.response.ResponseBuilder;
import org.alexdev.duckhttpd.server.connection.WebConnection;
import org.alexdev.duckhttpd.util.MimeType;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ImagerController {
    private static volatile boolean loaded;

    public static void imager_redirect(WebConnection webConnection) {
        try {
            loadAvatara();

            String requestUri = webConnection.request().uri();
            String path = requestUri.split("\\?", 2)[0];

            if (path.startsWith("/habbo-imaging/avatar/")) {
                sendAvatar(webConnection, legacyAvatarParameters(path));
                return;
            }

            if (path.equals("/habbo-imaging/avatarimage")) {
                sendAvatar(webConnection, parseQuery(requestUri));
                return;
            }

            if (path.startsWith("/habbo-imaging/badge/")) {
                sendBadge(webConnection, path);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendNoContent(webConnection);
    }

    private static void loadAvatara() {
        if (loaded) {
            return;
        }

        synchronized (ImagerController.class) {
            if (loaded) {
                return;
            }

            FiguredataReader.getInstance().load();
            LegacyFiguredataReader.getInstance().load();
            ManifestReader.getInstance().load();
            loaded = true;
        }
    }

    private static void sendAvatar(WebConnection webConnection, Map<String, String> parameters) {
        String figure = parameters.get("figure");

        if (figure == null || figure.isBlank()) {
            sendNoContent(webConnection);
            return;
        }

        Avatar avatar = new Avatar(
                FiguredataReader.getInstance(),
                figure,
                parameters.getOrDefault("size", "b"),
                getInt(parameters, "direction", 2),
                getInt(parameters, "head_direction", 2),
                parameters.getOrDefault("action", "std"),
                parameters.getOrDefault("gesture", "std"),
                getBoolean(parameters, "headonly", false),
                getInt(parameters, "frame", 1),
                getInt(parameters, "crr", -1),
                getBoolean(parameters, "crop", false)
        );

        byte[] image = avatar.run();

        if (image == null || image.length == 0) {
            sendNoContent(webConnection);
            return;
        }

        FullHttpResponse response = ResponseBuilder.create(HttpResponseStatus.OK, MimeType.getContentType("png"), image);
        webConnection.send(response);
    }

    private static void sendBadge(WebConnection webConnection, String path) {
        String badgeCode = path.substring("/habbo-imaging/badge/".length());
        RenderType renderType = RenderType.PNG;
        String contentType = MimeType.getContentType("png");

        if (badgeCode.endsWith(".gif")) {
            badgeCode = badgeCode.substring(0, badgeCode.length() - 4);
            renderType = RenderType.GIF;
            contentType = MimeType.getContentType("gif");
        } else if (badgeCode.endsWith(".png")) {
            badgeCode = badgeCode.substring(0, badgeCode.length() - 4);
        }

        if (badgeCode.isBlank()) {
            sendNoContent(webConnection);
            return;
        }

        BadgeSettings settings = new BadgeSettings();
        settings.setShockwaveBadge(true);
        settings.setRenderType(renderType);
        settings.setForceWhiteBackground(false);

        byte[] image = Badge.parseBadgeData(settings, badgeCode).render();

        if (image == null || image.length == 0) {
            sendNoContent(webConnection);
            return;
        }

        FullHttpResponse response = ResponseBuilder.create(HttpResponseStatus.OK, contentType, image);
        webConnection.send(response);
    }

    private static Map<String, String> legacyAvatarParameters(String path) {
        String value = path.substring("/habbo-imaging/avatar/".length());

        if (value.contains(",")) {
            value = value.split(",", 2)[0];
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("figure", value);
        parameters.put("size", "b");
        parameters.put("direction", "3");
        parameters.put("head_direction", "3");
        parameters.put("gesture", "sml");
        parameters.put("frame", "1");
        return parameters;
    }

    private static Map<String, String> parseQuery(String requestUri) {
        Map<String, String> parameters = new HashMap<>();
        String[] parts = requestUri.split("\\?", 2);

        if (parts.length < 2) {
            return parameters;
        }

        for (String pair : parts[1].split("&")) {
            String[] keyValue = pair.split("=", 2);
            String key = decode(keyValue[0]);
            String value = keyValue.length > 1 ? decode(keyValue[1]) : "";
            parameters.put(key, value);
        }

        return parameters;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private static int getInt(Map<String, String> parameters, String key, int defaultValue) {
        try {
            return Integer.parseInt(parameters.getOrDefault(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static boolean getBoolean(Map<String, String> parameters, String key, boolean defaultValue) {
        String value = parameters.get(key);

        if (value == null) {
            return defaultValue;
        }

        return value.equals("1") || value.equalsIgnoreCase("true");
    }

    private static void sendNoContent(WebConnection webConnection) {
        FullHttpResponse response = ResponseBuilder.create(
                HttpResponseStatus.NO_CONTENT, MimeType.getContentType("png"), new byte[0]
        );
        webConnection.send(response);
    }
}
