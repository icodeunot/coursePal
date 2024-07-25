package android.scheduler.studyBuddy.Helper;

import android.content.Context;
import android.content.Intent;
import android.scheduler.studyBuddy.Entity.Student;
import android.scheduler.studyBuddy.R;
import android.scheduler.studyBuddy.UI.StudentDetails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> mStudents;
    private final Context context;
    private final LayoutInflater mInflater;

    public StudentAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }
    public class StudentViewHolder extends RecyclerView.ViewHolder {

        private final TextView studentItemView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentItemView = itemView.findViewById(R.id.studentListItemTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Student current = mStudents.get(position);
                    Intent intent = new Intent(context, StudentDetails.class);
                    intent.putExtra("studentID", current.getStudentID());
                    intent.putExtra("studentFirst", current.getStudentFirst());
                    intent.putExtra("studentLast", current.getStudentLast());
                    intent.putExtra("studentPhone", current.getStudentPhone());
                    intent.putExtra("studentEmail", current.getStudentEmail());
                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public StudentAdapter.StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.student_list_item, parent, false);
        return new StudentViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.StudentViewHolder holder, int position) {
        if (mStudents != null) {
            Student current = mStudents.get(position);
            String name = current.getStudentFirst() + " " + current.getStudentLast();
            holder.studentItemView.setText(name);
        } else {
            holder.studentItemView.setText("No Student Name");
        }
    }
    @Override
    public int getItemCount() {
        if(mStudents != null) {
            return mStudents.size();
        }
        else {
            return 0;
        }
    }
    public void setStudents(List<Student> students) {
        mStudents = students;
        notifyDataSetChanged();
    }
}
