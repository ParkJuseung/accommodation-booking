package com.test.booking;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class CalendarService {

    public void showCalendar(int accommodationId, int year, int month, BookingService bookingService) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        List<LocalDate> bookedDates = bookingService.getBookedDatesByAccommodationId(accommodationId);
        System.out.println("\n╔══════════════════════════════════════════════════════╗");

        System.out.println("=== " + year + "년 " + month + "월 ===");
        System.out.println("일\t월\t화\t수\t목\t금\t토");

        // 첫 날 요일에 따라 공백 출력
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // 1(월) ~ 7(일)
        for (int i = 1; i < dayOfWeek; i++) {
            System.out.print("\t");
        }

        // 날짜 출력
        for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
            if (bookedDates.contains(date)) {
                // 예약된 날짜는 대괄호로 표시
                System.out.print("[" + date.getDayOfMonth() + "]\t");
            } else {
                // 예약 가능한 날짜는 일반 숫자로 표시
                System.out.print(date.getDayOfMonth() + "\t");
            }

            // 줄 바꿈 처리 (토요일 다음에 줄 바꿈)
            if (date.getDayOfWeek().getValue() == 7) {
                System.out.println();
            }
        }

        System.out.println(); // 마지막 줄 바꿈
    }

    public void showCalendarWithCheckInDate(int accommodationId, String checkInDate, String checkOutDate, BookingService bookingService) {
        LocalDate checkIn = LocalDate.parse(checkInDate);
        LocalDate checkOut = LocalDate.parse(checkOutDate);

        int year = checkIn.getYear();
        int month = checkIn.getMonthValue();

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        List<LocalDate> bookedDates = bookingService.getBookedDatesByAccommodationId(accommodationId);

        System.out.println("┃\t\t╔══════════════════════════════════════════════════════╗\t\t┃");
        System.out.printf("┃\t\t║\t\t\t\t\t   %d년 %02d월\t\t\t\t\t   ║\t\t┃\n", year, month);
        System.out.println("┃\t\t╠══════════════════════════════════════════════════════╣\t\t┃");
        System.out.println("┃\t\t║\t\t\t[일]\t[월]\t[화]\t[수]\t[목]\t[금]\t[토]\t\t\t\t   ║\t\t┃");

        // 첫 날 요일에 따라 공백 출력
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7; // 0(일) ~ 6(토)
        System.out.print("┃\t\t║\t\t\t");
        for (int i = 0; i < dayOfWeek; i++) {
            System.out.print("\t");
        }

        // 날짜 출력
        for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {

            if (!date.isBefore(checkIn) && !date.isAfter(checkOut.minusDays(1))) {
                // 체크인~체크아웃 범위는 ■로 표시
                System.out.printf("\u001B[33m%2d\u001B[0m\t", date.getDayOfMonth());
            } else if (bookedDates.contains(date)) {
                // 예약된 날짜는 대괄호로 표시
                System.out.printf("\u001B[31m%2d\u001B[0m\t", date.getDayOfMonth()+1);
            } else {
                // 예약 가능한 날짜는 일반 숫자로 표시
                System.out.printf("\u001B[32m%2d\u001B[0m\t", date.getDayOfMonth());
            }

            // 줄 바꿈 처리 (토요일 다음에 줄 바꿈)
            if (date.getDayOfWeek().getValue() % 7 == 6) {
                System.out.println("\t\t\t   ║\t\t┃"); // 줄의 끝에 ║ 추가
                if (!date.equals(lastDayOfMonth)) {
                    System.out.print("┃\t\t║\t\t\t"); // 새 줄의 시작에 ║ 추가
                }
            }
        }

        // 마지막 줄이 토요일로 끝나지 않을 경우 남은 공간을 채우고 닫기
        if (lastDayOfMonth.getDayOfWeek().getValue() % 7 != 6) {
            int remainingDays = 6 - lastDayOfMonth.getDayOfWeek().getValue();
            for (int i = 0; i < remainingDays; i++) {
                System.out.print("\t");
            }
            System.out.println("\t\t\t   ║\t\t┃"); // 줄의 끝에 ║ 추가
        }


        System.out.println("┃\t\t╚══════════════════════════════════════════════════════╝\t\t┃");
    }

}
