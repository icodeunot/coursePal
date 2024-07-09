package android.scheduler.johnsalazar.Helper;

import android.content.Context;
import android.scheduler.johnsalazar.Entity.Student;
import android.scheduler.johnsalazar.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private List<Student> mStudents;
    private final Context context;
    private final LayoutInflater mInflater;

    public SearchAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }
    public class SearchViewHolder extends RecyclerView.ViewHolder {
        private final TextView studentFirstView;
        private final TextView studentPhoneView;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            studentFirstView = itemView.findViewById(R.id.student_first);
            studentPhoneView = itemView.findViewById(R.id.student_phone);
        }
    }
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.student_search_item, parent, false);
        return new SearchViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        if (mStudents != null) {
            Student current = mStudents.get(position);
            holder.studentFirstView.setText(current.getStudentFirst());
            holder.studentPhoneView.setText(current.getStudentPhone());
        } else {
            holder.studentFirstView.setText("No Name");
            holder.studentPhoneView.setText("No Phone");
        }
    }
    @Override
    public int getItemCount() {
        if (mStudents != null) {
            return mStudents.size();
        } else {
            return 0;
        }
    }
    public void setStudents(List<Student> students) {
        mStudents = students;
        notifyDataSetChanged();
    }
}
