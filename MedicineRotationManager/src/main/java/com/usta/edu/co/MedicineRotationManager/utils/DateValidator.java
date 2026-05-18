package com.usta.edu.co.MedicineRotationManager.utils;

import java.time.LocalDate;

public final class DateValidator {

    private DateValidator() {
    }

    /**
     * Valida que la fecha inicial no sea posterior a la fecha final.
     */
    public static void validateDateRange(
            LocalDate startDate,
            LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                    "Dates cannot be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date");
        }
    }

    /**
     * Valida que una fecha no sea futura.
     */
    public static void validateNotFutureDate(
            LocalDate date) {

        if (date == null) {
            throw new IllegalArgumentException(
                    "Date cannot be null");
        }

        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Date cannot be in the future");
        }
    }

    /**
     * Valida que una fecha no sea pasada.
     */
    public static void validateNotPastDate(
            LocalDate date) {

        if (date == null) {
            throw new IllegalArgumentException(
                    "Date cannot be null");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Date cannot be in the past");
        }
    }

    /**
     * Valida que dos fechas no sean iguales.
     */
    public static void validateDifferentDates(
            LocalDate firstDate,
            LocalDate secondDate) {

        if (firstDate == null || secondDate == null) {
            throw new IllegalArgumentException(
                    "Dates cannot be null");
        }

        if (firstDate.isEqual(secondDate)) {
            throw new IllegalArgumentException(
                    "Dates cannot be equal");
        }
    }

    /**
     * Valida que una fecha esté dentro de un rango.
     */
    public static void validateDateInsideRange(
            LocalDate date,
            LocalDate startDate,
            LocalDate endDate) {

        validateDateRange(startDate, endDate);

        if (date.isBefore(startDate)
                || date.isAfter(endDate)) {

            throw new IllegalArgumentException(
                    "Date is outside the allowed range");
        }
    }

    /**
     * Valida que la fecha sea hoy o posterior.
     */
    public static void validateTodayOrFuture(
            LocalDate date) {

        if (date == null) {
            throw new IllegalArgumentException(
                    "Date cannot be null");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Date must be today or future");
        }
    }

    /**
     * Valida que la fecha sea hoy o anterior.
     */
    public static void validateTodayOrPast(
            LocalDate date) {

        if (date == null) {
            throw new IllegalArgumentException(
                    "Date cannot be null");
        }

        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Date must be today or past");
        }
    }
}