package cieslak.matty.worklog.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cieslak.matty.worklog.R
import cieslak.matty.worklog.helper.DatabaseHandler
import cieslak.matty.worklog.model.StartEndEnum
import cieslak.matty.worklog.model.WorkdayEntry
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class LogWorkActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var minute: Int = 0
    private var hour: Int = 0
    private var currentBtn = StartEndEnum.START
    private var startDateTime = LocalDateTime.MIN
    private var endDateTime = LocalDateTime.MIN
    private val readableFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_work)
        //disable save button
        val saveButton: Button = findViewById(R.id.save_work_entry_btn)
        saveButton.isClickable = false
    }

    fun selectDateTime(v: View) {
        when (v.id) {
            R.id.start_date_btn -> {
                currentBtn = StartEndEnum.START
                showDatePicker()
            }
            R.id.end_date_btn -> {
                currentBtn = StartEndEnum.END
                showDatePicker()
            }
        }
    }

    private fun showDatePicker() {
        val calendar: Calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this@LogWorkActivity,
            this@LogWorkActivity,
            calendar.get(Calendar.HOUR),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        this.minute = minute
        this.hour = hourOfDay

        val calendar: Calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this@LogWorkActivity,
            this@LogWorkActivity,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        when (currentBtn) {
            StartEndEnum.START -> {
                startDateTime = LocalDateTime.of(year, month, day, hour, minute)
                val textView: TextView = findViewById(R.id.start_date_lbl)
                textView.text = startDateTime.format(readableFormatter).toString()
            }
            StartEndEnum.END -> {
                endDateTime = LocalDateTime.of(year, month, day, hour, minute)
                val textView: TextView = findViewById(R.id.end_date_lbl)
                textView.text = endDateTime.format(readableFormatter).toString()
            }
        }
        if (startDateTime != LocalDateTime.MIN && endDateTime != LocalDateTime.MIN) {
            val textView: TextView = findViewById(R.id.time_worked_lbl)
            if (endDateTime.isAfter(startDateTime)) {
                val difference = Duration.between(startDateTime, endDateTime)
                // get minutes and hours
                val mins = difference.toMinutes() - (difference.toHours() * 60)
                val hrs = difference.toHours()
                val diffHours = if (hrs < 10) "0$hrs" else hrs
                val diffMinutes = if (mins < 10) "0$mins" else mins
                textView.text = String.format("Worked for %s:%s", diffHours, diffMinutes)

                //enable save button
                val saveButton: Button = findViewById(R.id.save_work_entry_btn)
                saveButton.isClickable = true
                saveButton.setBackgroundColor(resources.getColor(R.color.green_500, null))
            } else {
                textView.text = getString(R.string.start_before_end_txt)

                //disable save button
                val saveButton: Button = findViewById(R.id.save_work_entry_btn)
                saveButton.setBackgroundColor(resources.getColor(R.color.grey_700, null))
                saveButton.isClickable = false
            }
        }
    }

    fun clickedCancel(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun saveNewLogEntry(view: View) {
        val databaseHandler = DatabaseHandler(this)
        databaseHandler.addEntry(WorkdayEntry(startDateTime, endDateTime))

        Toast.makeText(this, "Saved new entry", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}