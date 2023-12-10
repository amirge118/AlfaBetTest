package org.example.event;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EventReminder {

    private int TIME = 30;
    private final ScheduledExecutorService scheduler;
    public EventReminder(ScheduledExecutorService scheduler){
        this.scheduler = scheduler;
    }
    public void scheduler(Date eventTime, String eventName) {
        long timeDifference = eventTime.getTime() - System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(TIME);
        if (timeDifference > 0) {
            // Schedule a reminder 30 minutes before the event
            scheduler.schedule(new ReminderTask(eventName), timeDifference, TimeUnit.MILLISECONDS);

            // Schedule the actual event
            scheduler.schedule(() -> System.out.println("Event: " + eventName + " at " + eventTime),
                    timeDifference + TimeUnit.MINUTES.toMillis(1), TimeUnit.MILLISECONDS);
            System.out.println("Event: " + eventName + " reminder set");
        } else {
            System.out.println("Event: " + eventName + " has already passed.");
        }
    }

    private static class ReminderTask implements Runnable {
        private final String eventName;

        ReminderTask(String eventName) {
            this.eventName = eventName;
        }

        @Override
        public void run() {
            // Send a reminder 30 minutes before the event
            System.out.println("Reminder: 30 minutes until Event: " + eventName);
        }
    }

    private void setTime(int time) {
        TIME = time;
    }
}
