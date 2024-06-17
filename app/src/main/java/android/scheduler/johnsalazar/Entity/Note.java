package android.scheduler.johnsalazar.Entity;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int noteID;
    private String noteText;
    private int courseID;

    public Note(int noteID, String noteText, int courseID) {
        this.noteID = noteID;
        this.noteText = noteText;
        this.courseID = courseID;
    }

    @Ignore
    public Note(int noteID, String noteText) {
        this.noteID = noteID;
        this.noteText = noteText;
    }

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }
}
