package com.example.demo.services.other;

import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.repositories.other.TimeOfTheWeekRepository;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

// Сервіс TimeOfTheWeekService містить бізнес-операції для модуля «часові слоти тижня».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class TimeOfTheWeekService {
    private final TimeOfTheWeekRepository timeOfTheWeekRepository;
    private TimesService timesService;
    // Отримує через Spring залежності TimeOfTheWeekRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «часові слоти тижня» без ручного створення об’єктів.
    @Autowired
    public TimeOfTheWeekService(TimeOfTheWeekRepository timeOfTheWeekRepository, TimesService timesService) {
        this.timeOfTheWeekRepository = timeOfTheWeekRepository;
        this.timesService = timesService;
    }
    // Знаходить часовий слот за днем тижня і годиною.
    // Метод потрібен старішим сценаріям, де хвилини ще не передаються окремо.
    public TimeOfTheWeek findByDayOfTheWeekAndTimeOfTheDay(int day, int hour){
        for(String time : timesService.getTimes().split(",")){
            int dayRet = Integer.parseInt(time.split("-")[1]);
            int hourRet = Integer.parseInt(time.split("-")[2]);
            if(day == dayRet && hour == hourRet){
                int id = Integer.parseInt(time.split("-")[0]);
                return  new TimeOfTheWeek(id, day, hour, 0);
            }
        }
        return new TimeOfTheWeek(1, 1, 8, 0);
    }
    // Знаходить точний часовий слот за днем тижня, годиною і хвилиною.
    // Його використовують форми створення розкладу, де час може бути 8:00 або 8:30.
    public TimeOfTheWeek findByDayOfTheWeekAndTimeOfTheDayAndMinute(int day, int hour, int minute){
        for(String time : timesService.getTimes().split(",")){
            int minuteRet = Integer.parseInt(time.split("-")[3]);
            int dayRet = Integer.parseInt(time.split("-")[1]);
            int hourRet = Integer.parseInt(time.split("-")[2]);
            if(day == dayRet && hour == hourRet && minute == minuteRet){
                int id = Integer.parseInt(time.split("-")[0]);
                return  new TimeOfTheWeek(id, day, hour, minute);
            }
        }
        return new TimeOfTheWeek(1, 1, 8, 0);
    }
    // Повертає всі часові слоти тижня з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
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
    // Знаходить один запис модуля «часові слоти тижня» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public TimeOfTheWeek getById(Integer id) {
        for(String time : timesService.getTimes().split(",")){
            int idRet = Integer.parseInt(time.split("-")[0]);
            if(id == idRet){
                int dayRet = Integer.parseInt(time.split("-")[1]);
                int hourRet = Integer.parseInt(time.split("-")[2]);
                int minuteRet = Integer.parseInt(time.split("-")[3]);
                return  new TimeOfTheWeek(id, dayRet, hourRet, minuteRet);
            }
        }
        return new TimeOfTheWeek(1, 1, 8, 0);
//        return timeOfTheWeekRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("TimeOfTheWeek not found with id: " + id));
    }
    public List<String> compileWeekDays(){
        LocalDate now = LocalDate.now();
        now = now.minusWeeks(2);
        List<String> weekDaysTemp = new ArrayList<>();
        weekDaysTemp.add("ПОН");
        weekDaysTemp.add("ВІВТ");
        weekDaysTemp.add("СЕР");
        weekDaysTemp.add("ЧЕТ");
        weekDaysTemp.add("ПЯТ");
        weekDaysTemp.add("СУБ");
        weekDaysTemp.add("НЕД");
        List<String> weekDays = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (String weekDay : weekDaysTemp) {
                weekDays.add(weekDaysTemp.get(now.getDayOfWeek().getValue() - 1) + " " + String.format("%02d", now.getDayOfMonth()) + "." + String.format("%02d", now.getMonthValue()));
                now = now.plusDays(1);
            }
        }
        return weekDays;
    }
    // Зберігає новий запис модуля «часові слоти тижня».
    // Метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
//    public TimeOfTheWeek create(TimeOfTheWeek timeOfTheWeek) {
//        return timeOfTheWeekRepository.save(timeOfTheWeek);
//    }

    // Оновлює існуючий запис модуля «часові слоти тижня».
    // Спочатку знаходить поточну сутність, переносить поля dayOfTheWeek, timeOfTheDay і зберігає її назад у репозиторій.
//    public TimeOfTheWeek update(Integer id, TimeOfTheWeek timeOfTheWeek) {
//        TimeOfTheWeek existing = getById(id);
//        existing.setDayOfTheWeek(timeOfTheWeek.getDayOfTheWeek());
//        existing.setTimeOfTheDay(timeOfTheWeek.getTimeOfTheDay());
//        return timeOfTheWeekRepository.save(existing);
//    }
    // Видаляє запис модуля «часові слоти тижня» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
//    public void deleteById(Integer id) {
//        getById(id);
//        timeOfTheWeekRepository.deleteById(id);
//    }
    // Перетворює id з checkbox-ів форми на об’єкти TimeOfTheWeek.
    // Якщо список порожній або null, повертає всі слоти, щоб форма не залишилася без варіантів.
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
    // Стискає послідовні години одного дня у компактні діапазони.
    // Наприклад, набір 8, 9, 10 перетворюється на один проміжок для зручного показу в листах і профілях.
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
    // Групує вибрані часові слоти за назвами днів тижня.
    // Після групування години стискаються через compressRanges, щоб шаблон показував розклад охайними блоками.
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
