package com.example.lab7

class FieldOfStudy {

    var id: Int = 0
    var fieldOfStudyName: String? = null
    var specialisation: String? = null
    var amountOfStudents: Int = 0

    constructor(id: Int, fieldOfStudyName: String, specialisation: String, amountOfStudents: Int) {
        this.id = id
        this.fieldOfStudyName = fieldOfStudyName
        this.specialisation = specialisation
        this.amountOfStudents = amountOfStudents
    }

    constructor(fieldOfStudyName: String, specialisation: String, amountOfStudents: Int) {
        this.fieldOfStudyName = fieldOfStudyName
        this.specialisation = specialisation
        this.amountOfStudents = amountOfStudents
    }
}