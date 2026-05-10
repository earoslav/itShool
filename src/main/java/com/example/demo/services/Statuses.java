package com.example.demo.services;

// Enum зібрав статуси, які сервіси повертають після дій з уроками, курсами, студентами й викладачами.
// Контролери передають ці значення у redirect-и, щоб сторінки показували зрозумілий результат операції.
public enum Statuses {
    LESSON_EDITED,
    LESSON_DELETED,
    EXISTS,
    NO_COURSES,
    NO_FREE_TIMES,
    NO_PASS,
    TEACHER_ADDED,
    GENERAL,
    TEACHER_EDITED,
    STUDENT_ADDED,
    STUDENT_DELETED,
    LESSON_AFTER_ACCEPTED_TIME,
    LESSON_OVERLAP,
    DATE_INCONSISTENCY,
    COURSE_DELETED,
    LESSON_BEFORE_NOW,
    COURSE_NOT_FOUND,
    TIME_NOT_EMPTY,
    ITEM_NOT_PICKED,
    TOO_EARLY,
    LESSON_STATUS_CHANGED,
    LESSON_ADDED,
    COURSE_ADDED,
    STUDENT_EDITED,
    STUDENT_NOT_FOUND,
    TOO_LATE,
    WAS,
    WILL,
    WASNT,
    WONT,
    SUCCESS,
    DURATION_NULL,
    TEACHER_NOT_FOUND
}
