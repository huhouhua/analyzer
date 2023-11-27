package com.ruijie.job.notify;

public class NotificationContent {

    private  String text;
    public  NotificationContent(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "NotificationContent{" +
                "text='" + text + '\'' +
                '}';
    }
}
