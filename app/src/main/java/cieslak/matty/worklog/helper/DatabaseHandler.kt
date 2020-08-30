package cieslak.matty.worklog.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import cieslak.matty.worklog.model.WorkdayEntry
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private val dbFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$ID INTEGER PRIMARY KEY, " +
                "$START_DATETIME TEXT, " +
                "$END_DATETIME TEXT" +
                ")"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Called when db needs to be upgraded
    }

    fun addEntry(workdayEntry: WorkdayEntry): Boolean {

        val db = this.writableDatabase
        val values = ContentValues()
        values.put(
            START_DATETIME,
            workdayEntry.start.format(dbFormatter)
        )
        values.put(
            END_DATETIME,
            workdayEntry.end.format(dbFormatter)
        )
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.v("DB", "Inserted new entry with id: $success")
        return (Integer.parseInt("$success") != -1)
    }

    fun getEntries(): List<WorkdayEntry> {

        val entries: MutableList<WorkdayEntry> = mutableListOf()
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getColumnIndex(ID);
                    val start =
                        LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(START_DATETIME)))
                    val end =
                        LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(END_DATETIME)))
                    entries.add(WorkdayEntry(id, start, end))
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return entries
    }

    companion object {
        private val DB_NAME = "WorklogDb"
        private val DB_VERSION = 1
        private val TABLE_NAME = "WorkEntries"
        private val ID = "id"
        private val START_DATETIME = "start"
        private val END_DATETIME = "end"
    }
}