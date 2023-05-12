package com.osrsclannotifcations.notifications;

import java.util.Map;

public class Notification {
    public String message;
    public String rsn;
    public NotificationType type = null;
    public Notification(){}
    public Notification(String _rsn, String _message, Map<NotificationType, String> compareMap)
    {
        message = _message;
        rsn = _rsn;

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
