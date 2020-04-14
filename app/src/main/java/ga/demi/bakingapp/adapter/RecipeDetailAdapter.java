package ga.demi.bakingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ga.demi.bakingapp.R;
import ga.demi.bakingapp.model.Step;

public final class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.ViewHolder> {

    private List<Step> stepsList;
    private RecipeDetailItemClickListener clickListener;

    public RecipeDetailAdapter(List<Step> stepsList, RecipeDetailItemClickListener clickListener) {
        this.stepsList = stepsList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_recipe_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDetailAdapter.ViewHolder holder, int position) {
        Step currentStep = stepsList.get(position);
        holder.bind(currentStep);
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_recipe_description)
        TextView recipeDescriptionText;

        @BindView(R.id.image_video)
        ImageView imageVideo;


        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Step item) {
            if (item.getVideoURL() == null || item.getVideoURL().equals("")) {
                imageVideo.setVisibility(View.GONE);
            } else {
                imageVideo.setVisibility(View.VISIBLE);
            }

            recipeDescriptionText.setText(item.getId() + ".   " + item.getShortDescription());
        }

        @Override
        public void onClick(View v) {
            clickListener.onRecipeDetailItemClick(getAdapterPosition());
        }
    }

    public void setStepsList(List<Step> stepsList) {
        this.stepsList = stepsList;
        notifyDataSetChanged();
    }

    public interface RecipeDetailItemClickListener {
        void onRecipeDetailItemClick(int position);
    }
}