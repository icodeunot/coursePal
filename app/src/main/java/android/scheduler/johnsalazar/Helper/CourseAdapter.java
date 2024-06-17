package android.scheduler.johnsalazar.Helper;

import android.content.Context;
import android.content.Intent;
import android.scheduler.johnsalazar.Entity.Course;
import android.scheduler.johnsalazar.R;
import android.scheduler.johnsalazar.UI.AddTerm;
import android.scheduler.johnsalazar.UI.CourseDetails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> mCourses;
    private AddTerm termContext;
    private final Context context;
    private final LayoutInflater mInflater;

    public CourseAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public CourseAdapter(Context termContext, List<Course> courses) {
        mInflater = LayoutInflater.from(termContext);
        this.context = termContext;
        this.mCourses = courses;
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseItemView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseItemView = itemView.findViewById(R.id.courseListItemTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Course current = mCourses.get(position);

                    Intent intent = new Intent(context, CourseDetails.class);
                    intent.putExtra("courseID", current.getCourseID());
                    intent.putExtra("termID", current.getTermID());
                    intent.putExtra("courseTitle", current.getCourseTitle());
                    intent.putExtra("courseStart", current.getCourseStart());
                    intent.putExtra("courseEnd", current.getCourseEnd());
                    intent.putExtra("courseStatus", current.getCourseStatus());
                    intent.putExtra("courseNote", current.getCourseNote());
                    intent.putExtra("instructorID", current.getInstructorID());

                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.course_list_item, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        if(mCourses != null) {
            Course current = mCourses.get(position);
            String name = current.getCourseTitle();
            holder.courseItemView.setText(name);
        }
        else {
            holder.courseItemView.setText("No Course Title");
        }
    }

    @Override
    public int getItemCount() {
        if(mCourses != null) {
            return mCourses.size();
        }
        else {
            return 0;
        }
    }

    public void setCourses(List<Course> courses) {
        mCourses = courses;
        notifyDataSetChanged();
    }
}
