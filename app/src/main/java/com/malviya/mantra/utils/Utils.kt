import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Helper function to get the current date and time as a string
     fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
        return dateFormat.format(Date())
    }