package com.example.demo.services;

import com.example.demo.models.TimeOfTheWeek;
import com.example.demo.repositories.TimeOfTheWeekRepository;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class TimeOfTheWeekService {
    private final TimeOfTheWeekRepository timeOfTheWeekRepository;

    public TimeOfTheWeekService(TimeOfTheWeekRepository timeOfTheWeekRepository) {
        this.timeOfTheWeekRepository = timeOfTheWeekRepository;
    }
    public TimeOfTheWeek findByDayOfTheWeekAndTimeOfTheDay(int day, int hour){
        return timeOfTheWeekRepository.findByDayOfTheWeekAndTimeOfTheDay(day,hour);
    }

    public List<TimeOfTheWeek> getAll() {
        return timeOfTheWeekRepository.findAll();
    }

    public TimeOfTheWeek getById(Integer id) {
        return timeOfTheWeekRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TimeOfTheWeek not found with id: " + id));
    }

    public TimeOfTheWeek create(TimeOfTheWeek timeOfTheWeek) {
        return timeOfTheWeekRepository.save(timeOfTheWeek);
    }

    public TimeOfTheWeek update(Integer id, TimeOfTheWeek timeOfTheWeek) {
        TimeOfTheWeek existing = getById(id);
        existing.setDayOfTheWeek(timeOfTheWeek.getDayOfTheWeek());
        existing.setTimeOfTheDay(timeOfTheWeek.getTimeOfTheDay());
        return timeOfTheWeekRepository.save(existing);
    }

    public void deleteById(Integer id) {
        getById(id);
        timeOfTheWeekRepository.deleteById(id);
    }
    public List<TimeOfTheWeek> getTimesOfTheWeekThroughIds(List<Integer> ids){
        if(ids!=null && !ids.isEmpty()){
            List<TimeOfTheWeek> freeTimes = new ArrayList<>();
            ids.stream().forEach(time-> freeTimes.add(getById(time)));
            return freeTimes;
        }else{
            List<TimeOfTheWeek> freeTimes = getAll();
            return freeTimes;
        }
    }
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

        // додаємо останній діапазон
        if (start == prev) {
            result.add(String.valueOf(start));
        } else {
            result.add(start + "-" + prev);
        }

        return result;
    }
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
//                    List<Integer> times = selectedFreeTimes.stream().filter(time->time.getDayOfTheWeek()== finalI).map(time->time.getTimeOfTheDay()).collect(Collectors.toList());
//                    String strTime = "";
//                    StringBuilder sb = new StringBuilder(strTime);
//                    for(int i1 = 0; i1<times.size()-2; i1++){
//
//                        if(times.get(i1)==times.get(i1+1)-1){
//                            if(sb.length()==0){
//                                sb.append(String.valueOf(times.get(i1))+"-"+"0");
//                                if(i1==times.size()-2){
//                                    sb.setCharAt(2, Character.highSurrogate(times.get(i1+1)));
//                                    strTimes.add(sb.toString());
//                                }
//                            }else{
//                                sb.setCharAt(2, Character.highSurrogate(times.get(i1)));
//                            }
//
//                        }else{
//                            if(sb.length()!=0){
//                                strTimes.add(sb.toString());
//                                strTime = "";
//                                sb =  new StringBuilder(strTime);
//                            }
//                            strTimes.add(String.valueOf(times.get(i1)));
//                            if(i1+1==times.size()-1){
//                                strTimes.add(String.valueOf(times.get(i1+1)));
//                            }
//                        }
//
//                    }
            switch(i){
                case 1: compiledTimes.put("Понеділок", strTimes);
                case 2: compiledTimes.put("Вівторок", strTimes);
                case 3: compiledTimes.put("Середа", strTimes);
                case 4: compiledTimes.put("Четвер", strTimes);
                case 5: compiledTimes.put("П'ятниця", strTimes);
                case 6: compiledTimes.put("Субота", strTimes);
                case 7: compiledTimes.put("Неділя", strTimes);
            }
        }
        return compiledTimes;

    }


}
