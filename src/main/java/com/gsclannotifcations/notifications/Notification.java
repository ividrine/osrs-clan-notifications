package com.gsclannotifcations.notifications;

import java.util.Map;

import static java.util.Map.entry;

public class Notification {
    public String message;
    public NotificationType type = null;
    public Notification(String _message, Map<NotificationType, String> compareMap)
    {
        message = _message;

        for (Map.Entry<NotificationType, String> entry : compareMap.entrySet())
        {
            if (message.toLowerCase().contains(entry.getValue()))
            {
                type = entry.getKey();
                break;
            }
        }
    }
}
