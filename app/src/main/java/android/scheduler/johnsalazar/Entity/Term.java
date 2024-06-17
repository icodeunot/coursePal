package android.scheduler.johnsalazar.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "terms")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int termID;
    private String termTitle;
    private String termStart;
    private String termEnd;

    public Term(int termID, String termTitle, String termStart, String termEnd) {
        this.termID = termID;
        this.termTitle = termTitle;
        this.termStart = termStart;
        this.termEnd = termEnd;
    }

    public String toString() {
        return termTitle;
    }
    public int getTermID() {
        return termID;
    }
    public String getTermTitle() {
        return termTitle;
    }
    public String getTermStart() {
        return termStart;
    }
    public String getTermEnd() {
        return termEnd;
    }
    public void setTermID(int termID) {
        this.termID = termID;
    }
    public void setTermTitle(String termTitle) {
        this.termTitle = termTitle;
    }
    public void setTermStart(String termStart) {
        this.termStart = termStart;
    }
    public void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
    }
}
