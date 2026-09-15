package org.example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkSheet {
    String name;
    List<DayOfWeek> workDays;

    public WorkSheet(String name, List<DayOfWeek> workDays) {
        this.name = name;
        this.workDays = workDays;
    }

    public String getName() {
        return name;
    }

    public List<DayOfWeek> getWorkDays() {
        return workDays;
    }

    public void test() {
        var denSheet = new WorkSheet("Денис",
                List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SUNDAY));
        var benSheet = new WorkSheet("Бен",
                List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY));
        var lisSheet = new WorkSheet("Лиза",
                List.of(DayOfWeek.THURSDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        var sheets = List.of(denSheet, benSheet, lisSheet);
        // TODO: Необходимо вывести на экран дни в которые работало более 1 человека
        sheets.stream()
                .flatMap(s-> s.workDays.stream())
                .collect(Collectors.groupingBy(d-> d,
                        Collectors.counting()))
                .entrySet()
                .stream()
                .filter(e-> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .forEach(System.out::println);
    }
}
