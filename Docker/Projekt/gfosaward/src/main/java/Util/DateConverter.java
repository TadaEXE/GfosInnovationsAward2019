/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;



/**
 *
 * @author Henry
 */
public class DateConverter
{
    
    public static Date utilDateToSqlDate(java.util.Date input)//Convert java.util.date to java.sql.date
    {
        java.sql.Date sDate = new java.sql.Date(input.getTime());//Create new sql.date object with ms count from util.date
        return sDate;
    }

    public static Date stringToSqlDate(String input) throws DateTimeParseException//Convert Date/LocalDate to Date useable in sql
    {
        Date outDate;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");//Create Formatter with standart german pattern
        LocalDate localDate = LocalDate.parse(input, dateTimeFormatter);//create Date from String after pattern
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");//Create Fromatter with sql useable pattern
        localDate.format(dateTimeFormatter);//format date to right sql pattern
        outDate = java.sql.Date.valueOf(localDate);//create actual sql date

        return outDate;
    }

    public static Date stringToSqlDate(String input, String inputPattern) throws DateTimeParseException//Convert Date/LocalDate with a defined pattern to Date useable in sql
    {
        Date outDate;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(inputPattern);//Create Formatter with given pattern
        LocalDate localDate = LocalDate.parse(input, dateTimeFormatter);//create Date from String after pattern
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");//Create Fromatter with sql useable pattern
        localDate.format(dateTimeFormatter);//format date to right sql pattern
        outDate = java.sql.Date.valueOf(localDate);//create actual sql date

        return outDate;
    }
}
