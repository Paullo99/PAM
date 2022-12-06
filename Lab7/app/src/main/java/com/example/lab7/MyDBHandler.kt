package com.example.lab7

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class MyDBHandler(
    context: Context, name: String?,
    factory: SQLiteDatabase.CursorFactory?, version: Int
) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_FIELDS_OF_STUDY_TABLE = ("CREATE TABLE " +
                TABLE_FIELD_OF_STUDY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_FIELD_OF_STUDY_NAME + " TEXT,"
                + COLUMN_SPECIALISATION + " TEXT,"
                + COLUMN_AMOUNT_OF_STUDENTS + " INTEGER" + ")")
        db.execSQL(CREATE_FIELDS_OF_STUDY_TABLE)
        db.execSQL("INSERT INTO $TABLE_FIELD_OF_STUDY ($COLUMN_FIELD_OF_STUDY_NAME, $COLUMN_SPECIALISATION, $COLUMN_AMOUNT_OF_STUDENTS) " +
                "VALUES (\"Informatyka Techniczna\",\"INS \nISK\", 90)")
        db.execSQL("INSERT INTO $TABLE_FIELD_OF_STUDY ($COLUMN_FIELD_OF_STUDY_NAME, $COLUMN_SPECIALISATION, $COLUMN_AMOUNT_OF_STUDENTS) " +
                "VALUES (\"Informatyka Algorytmiczna\",\"CE \nALG\", 20)")
        db.execSQL("INSERT INTO $TABLE_FIELD_OF_STUDY ($COLUMN_FIELD_OF_STUDY_NAME, $COLUMN_SPECIALISATION, $COLUMN_AMOUNT_OF_STUDENTS) " +
                "VALUES (\"Telekomunikacja\",\"TIM \nTSM\", 40)")
        db.execSQL("INSERT INTO $TABLE_FIELD_OF_STUDY ($COLUMN_FIELD_OF_STUDY_NAME, $COLUMN_SPECIALISATION, $COLUMN_AMOUNT_OF_STUDENTS) " +
                "VALUES (\"Informatyka Stosowana\",\"IO\nPSI\nZSTI\", 40)")
    }

    override fun onUpgrade(
        db: SQLiteDatabase, oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FIELD_OF_STUDY")
        onCreate(db)
    }

    companion object {

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "studyDB.db"
        val TABLE_FIELD_OF_STUDY = "fieldOfStudy"

        val COLUMN_ID = "_id"
        val COLUMN_FIELD_OF_STUDY_NAME = "fieldOfStudyName"
        val COLUMN_SPECIALISATION = "specialisation"
        val COLUMN_AMOUNT_OF_STUDENTS = "amountOfStudents"
    }

    fun addFieldOfStudy(fieldOfStudy: FieldOfStudy) {
        val db = this.writableDatabase
        val stmt =
            db.compileStatement("INSERT INTO $TABLE_FIELD_OF_STUDY (fieldOfStudyName, specialisation, amountOfStudents) VALUES (?, ?, ?)")

        stmt.bindString(1, fieldOfStudy.fieldOfStudyName)
        stmt.bindString(2, fieldOfStudy.specialisation)
        stmt.bindLong(3, fieldOfStudy.amountOfStudents.toLong())

        stmt.executeInsert()
        db.close()
    }

    fun findFieldOfStudy(fieldOfStudyName: String): FieldOfStudy? {
        val query =
            "SELECT * FROM $TABLE_FIELD_OF_STUDY WHERE $COLUMN_FIELD_OF_STUDY_NAME =  \"$fieldOfStudyName\""

        val db = this.readableDatabase

        val cursor = db.rawQuery(query, null)

        var fieldOfStudy: FieldOfStudy? = null

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            val name = cursor.getString(1)
            val specialisation = cursor.getString(2)
            val amountOfStudents = Integer.parseInt(cursor.getString(3))
            fieldOfStudy = FieldOfStudy(id, name, specialisation, amountOfStudents)
            cursor.close()
        }

        db.close()
        return fieldOfStudy
    }

    fun editFieldOfStudy(fieldOfStudy: FieldOfStudy) {
        val query =
            "UPDATE $TABLE_FIELD_OF_STUDY SET $COLUMN_FIELD_OF_STUDY_NAME = \"${fieldOfStudy.fieldOfStudyName}\", " +
                    "$COLUMN_SPECIALISATION = \"${fieldOfStudy.specialisation}\"," +
                    "$COLUMN_AMOUNT_OF_STUDENTS = \"${fieldOfStudy.amountOfStudents}\"" +
                    " WHERE $COLUMN_ID = \"${fieldOfStudy.id}\""
        println(query)
        val db = this.writableDatabase
        db.execSQL(query)
        db.close()

        db.close()
    }

    fun findAllFieldsOfStudy(): ArrayList<FieldOfStudy> {
        val resultList = ArrayList<FieldOfStudy>()
        val query = "SELECT * FROM $TABLE_FIELD_OF_STUDY"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = Integer.parseInt(cursor.getString(0))
            val name = cursor.getString(1)
            val specialisation = cursor.getString(2)
            val amountOfStudents = Integer.parseInt(cursor.getString(3))
            val fieldOfStudy = FieldOfStudy(id, name, specialisation, amountOfStudents)
            resultList.add(fieldOfStudy)
        }
        cursor.close()
        db.close()
        return resultList
    }

    fun deleteFieldOfStudy(fieldOfStudyName: String): Boolean {

        var result = false

        val query =
            "SELECT * FROM $TABLE_FIELD_OF_STUDY WHERE $COLUMN_FIELD_OF_STUDY_NAME = \"$fieldOfStudyName\""

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(
                TABLE_FIELD_OF_STUDY, "$COLUMN_ID = ?",
                arrayOf(id.toString())
            )
            cursor.close()
            result = true
        }
        db.close()
        return result
    }
}