package com.epam.university.java.core.task042;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Task042Impl implements Task042 {
    @Override
    public BookingResponse checkAvailability(List<String> schedule, String currentTime) {
        if (schedule == null || currentTime == null) {
            throw new IllegalArgumentException();
        }

        LocalTime currentTimeInTimeFormat = LocalTime.parse(currentTime);
        // Check for the case of empty schedule (means that Alexander has no plans for a day).
        // If the current time is before 9:00 (start of the working day)
        // the proposed time is 9:00
        if (schedule.size() == 0) {
            if (currentTimeInTimeFormat.isBefore(LocalTime.of(9, 0))) {
                return new TimeProposalResponse("09:00");
            } else if (currentTimeInTimeFormat.isAfter(LocalTime.of(17, 59))) {
                return new BusyResponse();
            } else {
                return new AvailableResponse();
            }
        }

        List<LocalTime> scheduleInTimeFormat = new LinkedList<>();
        for (String s : schedule) {
            String[] splittedScheduleArray = s.split("-");
            List<String> splittedScheduleList = Arrays.asList(splittedScheduleArray);
            for (String time : splittedScheduleList) {
                LocalTime scheduleTime = LocalTime.parse(time);
                scheduleInTimeFormat.add(scheduleTime);
            }
        }

        int indexFrom = 0;
        // Checks for the non-empty schedule.
        // If the current time is before 9:00 (start of the working day)
        // and the first task in schedule is after 09:00
        // the proposed time is 9:00
        // else we need to add current time in the scheduleInTimeFormat List
        // for checking all tasks from the current time to get the "window"
        // or get conclusion that Alexander will be busy the whole day from the current time.
        // tip: in case when we added the current time in the even position,
        // it means that we've got in the schedule "window".
        if (currentTimeInTimeFormat.isBefore(scheduleInTimeFormat.get(0))) {
            if (scheduleInTimeFormat.get(0).isAfter(LocalTime.of(9, 0))) {
                return new TimeProposalResponse("09:00");
            } else {
                scheduleInTimeFormat.add(0, currentTimeInTimeFormat);
            }
        } else {
            for (int i = 0; i <= scheduleInTimeFormat.size() - 2; i++) {
                if (currentTimeInTimeFormat.isAfter(scheduleInTimeFormat.get(i))
                        && currentTimeInTimeFormat.isBefore(scheduleInTimeFormat.get(i + 1))) {
                    scheduleInTimeFormat.add(i + 1, currentTimeInTimeFormat);
                    if ((i + 1) % 2 == 0) {
                        return new AvailableResponse();
                    }
                    indexFrom = i + 1;
                }
            }
        }
        // check for the "window" in schedule.
        for (int i = indexFrom + 1; i < scheduleInTimeFormat.size() - 2; i += 2) {
            if (!(scheduleInTimeFormat.get(i).equals(scheduleInTimeFormat.get(i + 1)))) {
                return new TimeProposalResponse(String.valueOf(scheduleInTimeFormat.get(i)));
            }
        }
        // if we did not find a window in the schedule,
        // we need to check the time of the last activity in the schedule.
        // If it's before the end of the working day
        // the proposed time is the time of the last activity.
        if (scheduleInTimeFormat.get(scheduleInTimeFormat.size() - 1)
                .isBefore(LocalTime.of(18, 0))) {
            return new TimeProposalResponse(String
                    .valueOf(scheduleInTimeFormat
                            .get(scheduleInTimeFormat.size() - 1)));
        } else {
            return new BusyResponse();
        }
    }
}
