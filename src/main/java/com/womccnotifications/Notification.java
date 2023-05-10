package com.womccnotifications;

import java.util.Dictionary;
import java.util.Map;

import static java.util.Map.entry;

public class Notification {

    private Map<NotificationType, String[]> compareMap = Map.ofEntries
    (
        entry(NotificationType.GENERAL, new String[]{"quest", "achievement diary", "personal best", "collection log"}),
        entry(NotificationType.SKILLING, new String[]{"level"}),
        entry(NotificationType.PVM, new String[]{"received a drop", "combat achievement"}),
        entry(NotificationType.PVP, new String[]{"defeated"})
    );

    private Map<NotificationType, String> titleMap = Map.ofEntries
    (
        entry(NotificationType.GENERAL, "General"),
        entry(NotificationType.SKILLING, "Skilling"),
        entry(NotificationType.PVM, "PvP"),
        entry(NotificationType.PVP, "PvM")
    );

    public String title;

    public String message;

    NotificationType type = NotificationType.NONE;

    public Notification(String message)
    {
        message = message;

        outer: for (Map.Entry<NotificationType, String[]> entry : compareMap.entrySet())
        {
            for( String msgCompare : entry.getValue())
            {
                if (message.contains(msgCompare)) {
                    type = entry.getKey();
                    title = titleMap.get(type);
                    break outer;
                }
            }
        }
    }
}
