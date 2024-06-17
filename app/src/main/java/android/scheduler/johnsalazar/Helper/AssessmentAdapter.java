package android.scheduler.johnsalazar.Helper;

import android.content.Context;
import android.content.Intent;
import android.scheduler.johnsalazar.Entity.Assessment;
import android.scheduler.johnsalazar.Entity.Term;
import android.scheduler.johnsalazar.R;
import android.scheduler.johnsalazar.UI.AssessmentDetails;
import android.scheduler.johnsalazar.UI.TermDetails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    private List<Assessment> mAssessments;
    private final Context context;
    private final LayoutInflater mInflater;

    public AssessmentAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }
    public class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentItemView;

        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            assessmentItemView = itemView.findViewById(R.id.assessmentListItemTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Assessment current = mAssessments.get(position);
                    Intent intent = new Intent(context, AssessmentDetails.class);
                    intent.putExtra("courseID", current.getCourseID());
                    intent.putExtra("assessmentID", current.getAssessmentID());
                    intent.putExtra("assessmentTitle", current.getAssessmentTitle());
                    intent.putExtra("assessmentType", current.getAssessmentType());
                    intent.putExtra("assessmentStart", current.getStartDate());
                    intent.putExtra("assessmentEnd", current.getEndDate());
                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public AssessmentAdapter.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.assessment_list_item, parent, false);
        return new AssessmentAdapter.AssessmentViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentViewHolder holder, int position) {
        if (mAssessments != null) {
            Assessment current = mAssessments.get(position);
            String name = current.getAssessmentTitle();
            holder.assessmentItemView.setText(name);
        } else {
            holder.assessmentItemView.setText("No Assessment Name");
        }
    }
    @Override
    public int getItemCount() {
        if(mAssessments != null) {
            return mAssessments.size();
        }
        else {
            return 0;
        }
    }
    public void setAssessments(List<Assessment> assessments) {
        mAssessments = assessments;
        notifyDataSetChanged();
    }
}
