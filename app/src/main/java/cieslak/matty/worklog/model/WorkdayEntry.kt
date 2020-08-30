package cieslak.matty.worklog.model

import java.time.LocalDateTime

class WorkdayEntry {

    var id :Int = 0
    var start: LocalDateTime
    var end: LocalDateTime

    constructor(startDateTime: LocalDateTime, endDateTime: LocalDateTime) {

        this.start = startDateTime
        this.end = endDateTime
    }

    constructor(id: Int, startDateTime: LocalDateTime, endDateTime: LocalDateTime) {

        this.id = id
        this.start = startDateTime
        this.end = endDateTime
    }
}