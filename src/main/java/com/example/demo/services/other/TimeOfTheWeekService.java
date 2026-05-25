package com.example.demo.services.other;

import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.repositories.other.TimeOfTheWeekRepository;
import com.example.demo.services.other.TimesService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TimeOfTheWeekService contains business operations for the "weekly time slots" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class TimeOfTheWeekService {
    private final TimeOfTheWeekRepository timeOfTheWeekRepository;
    private final TimesService timesService;

    // Receives dependencies through Spring.
    @Autowired
    public TimeOfTheWeekService(TimeOfTheWeekRepository timeOfTheWeekRepository, TimesService timesService) {
        this.timeOfTheWeekRepository = timeOfTheWeekRepository;
        this.timesService = timesService;
    }

    // Finds a time slot by day of the week and hour.
    public TimeOfTheWeek findByDayOfTheWeekAndTimeOfTheDay(int day, int hour){
        return getAll().stream().filter(t -> t.getDayOfTheWeek() == day && t.getTimeOfTheDay() == hour).findFirst().get();
    }

    // Finds an exact time slot by day of the week, hour, and minute.
    public TimeOfTheWeek findByDayOfTheWeekAndTimeOfTheDayAndMinute(int day, int hour, int minute){
        return getAll().stream().filter(t -> t.getDayOfTheWeek() == day && t.getTimeOfTheDay() == hour && t.getMinute() == minute).findFirst().get();
    }
    public List<String> compileHours(){
        return Arrays.asList("8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30");
    }

    // Returns all weekly time slots from the repository.
    public List<TimeOfTheWeek> getAll() {
        List<TimeOfTheWeek> times = new ArrayList<>();
        for(String time : timesService.getTimes().split(",")){
            int id = Integer.parseInt(time.split("-")[0]);
            int day = Integer.parseInt(time.split("-")[1]);
            int hour = Integer.parseInt(time.split("-")[2]);
            int minute = Integer.parseInt(time.split("-")[3]);
            TimeOfTheWeek timeOfTheWeek = new TimeOfTheWeek(id, day, hour, minute);
            times.add(timeOfTheWeek);
        }
        return times;
    }

    // Finds a single record in the "weekly time slots" module by ID.
    public TimeOfTheWeek getById(Integer id) {
        return getAll().stream().filter(t -> t.getId() == id).findFirst().get();
    }
    public List<String> compileWeekDays(){
        LocalDate now = LocalDate.now();
        now = now.minusWeeks(2);
        List<String> weekDaysTemp = new ArrayList<>();
        weekDaysTemp.add("MON");
        weekDaysTemp.add("TUE");
        weekDaysTemp.add("WED");
        weekDaysTemp.add("THU");
        weekDaysTemp.add("FRI");
        weekDaysTemp.add("SAT");
        weekDaysTemp.add("SUN");
        List<String> weekDays = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (String weekDay : weekDaysTemp) {
                weekDays.add(weekDaysTemp.get(now.getDayOfWeek().getValue() - 1) + " " + String.format("%02d", now.getDayOfMonth()) + "." + String.format("%02d", now.getMonthValue()));
                now = now.plusDays(1);
            }
        }
        return weekDays;
    }
    // Saves a new record in the "weekly time slots" module.
    // This method is called after the controller has collected the entity from a form or a service has generated it automatically.
//    public TimeOfTheWeek create(TimeOfTheWeek timeOfTheWeek) {
//        return timeOfTheWeekRepository.save(timeOfTheWeek);
//    }

    // Updates an existing record in the "weekly time slots" module.
    // First finds the current entity, transfers dayOfTheWeek and timeOfTheDay fields, then saves it back to the repository.
//    public TimeOfTheWeek update(Integer id, TimeOfTheWeek timeOfTheWeek) {
//        TimeOfTheWeek existing = getById(id);
//        existing.setDayOfTheWeek(timeOfTheWeek.getDayOfTheWeek());
//        existing.setTimeOfTheDay(timeOfTheWeek.getTimeOfTheDay());
//        return timeOfTheWeekRepository.save(existing);
//    }
    // Deletes a record in the "weekly time slots" module by ID.
    // Before calling deleteById, the method reads the entity to ensure the deletion passes through the service layer and fails clearly if the ID is incorrect.
//    public void deleteById(Integer id) {
//        getById(id);
//        timeOfTheWeekRepository.deleteById(id);
//    }
    // Converts checkbox IDs from the form into TimeOfTheWeek objects.
    // If the list is empty or null, returns all slots to ensure the form is not left without options.
    public List<TimeOfTheWeek> getTimesOfTheWeekThroughIds(String idsStr){
        List<Integer> ids = new ArrayList<>();
        Arrays.stream(idsStr.split(",")).forEach(id->ids.add(Integer.valueOf(id)));
        List<TimeOfTheWeek> freeTimes = new ArrayList<>();
        if(ids!=null && !ids.isEmpty()){
            for(String time : timesService.getTimes().split(",")){
                int idRet = Integer.parseInt(time.split("-")[0]);
                if(ids.contains(idRet)){
                    int dayRet = Integer.parseInt(time.split("-")[1]);
                    int hourRet = Integer.parseInt(time.split("-")[2]);
                    int minuteRet = Integer.parseInt(time.split("-")[3]);
                    freeTimes.add(new TimeOfTheWeek(idRet, dayRet, hourRet, minuteRet));
                }
            }
            return freeTimes;
        }else{
            freeTimes = getAll();
            return freeTimes;
        }
    }
    // Compresses consecutive hours of the same day into compact ranges.
    // For example, the set 8, 9, 10 is converted into a single range for convenient display in emails and profiles.
    public List<String> compressRanges(List<String> input) {
        List<Integer> numbers = input.stream()
                .map(Integer::parseInt)
                .sorted()
                .toList();

        List<String> result = new ArrayList<>();

        int start = numbers.get(0);
        int prev = start;

        for (int i = 1; i < numbers.size(); i++) {
            int curr = numbers.get(i);

            if (curr == prev + 1) {
                prev = curr;
            } else {
                if (start == prev) {
                    result.add(String.valueOf(start));
                } else {
                    result.add(start + "-" + prev);
                }
                start = curr;
                prev = curr;
            }
        }

        // Add the last range
        if (start == prev) {
            result.add(String.valueOf(start));
        } else {
            result.add(start + "-" + prev);
        }

        return result;
    }
    // Groups selected time slots by weekday names.
    // After grouping, hours are compressed using compressRanges so that the template displays the schedule in neat blocks.
    public HashMap<String, List<String>> compileTimes(List<TimeOfTheWeek> selectedFreeTimes){
        HashMap<String, List<String>> compiledTimes = new HashMap<>();

        for(int i = 1; i<=7; i++){
            int finalI = i;
            List<String> strTimes = new ArrayList<>();
            List<String> finalInput = strTimes;
            List<Integer> times = selectedFreeTimes.stream().filter(time->time.getDayOfTheWeek()== finalI).map(time->time.getTimeOfTheDay()).collect(Collectors.toList());
            times.stream().forEach(time -> finalInput.add(String.valueOf(time)));
            if(times.size()!=0) {
                strTimes = compressRanges(finalInput);
            }
            switch(i){
                case 1: compiledTimes.put("Monday", strTimes);
                case 2: compiledTimes.put("Tuesday", strTimes);
                case 3: compiledTimes.put("Wednesday", strTimes);
                case 4: compiledTimes.put("Thursday", strTimes);
                case 5: compiledTimes.put("Friday", strTimes);
                case 6: compiledTimes.put("Saturday", strTimes);
                case 7: compiledTimes.put("Sunday", strTimes);
            }
        }
        return compiledTimes;

    }


}
