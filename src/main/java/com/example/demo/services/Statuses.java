package com.example.demo.services;

// Enum gathering statuses that services return after actions with lessons, courses, students, and teachers.
// Controllers pass these values in redirects so that pages show a clear result of the operation.
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
