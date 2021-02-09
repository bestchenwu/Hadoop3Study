package java8Study.unit12;

import java.time.LocalDate;
import java.time.Period;

public class PeriodTest {

    public static void main(String[] args) {
        Period period = Period.between(LocalDate.of(2014, 2, 22), LocalDate.of(2014, 3, 18));
        System.out.println("days:"+period.getDays());
        System.out.println("month:"+period.getMonths());
    }
}
