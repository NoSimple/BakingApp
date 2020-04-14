package ga.demi.bakingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ga.demi.bakingapp.R;
import ga.demi.bakingapp.model.Recipe;

public final class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> recipesList;
    private RecipeItemClickListener clickListener;

    public RecipeAdapter(List<Recipe> recipesList, RecipeItemClickListener listener) {
        this.recipesList = recipesList;
        clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(recipesList.get(position));
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }

    final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_text)
        TextView recipeText;

        @BindView(R.id.recipe_image)
        ImageView recipeImage;

        @BindView(R.id.step_text)
        TextView stepsText;

        @BindView(R.id.servings_text)
        TextView servingsText;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(Recipe item) {

            if (!item.getImage().isEmpty()) {
                Picasso.get().load(item.getImage()).error(R.drawable.baking_img).into(recipeImage);
            } else {
                recipeImage.setImageResource(R.drawable.baking_img);
            }

            recipeText.setText(item.getName());
            stepsText.setText("Steps: " + item.getSteps().size());
            servingsText.setText("Servings: " + item.getServings());
        }


        @Override
        public void onClick(View v) {
            clickListener.onRecipeItemClick(getAdapterPosition());
        }
    }

    public void notifyRecipesList(List<Recipe> recipesList) {
        this.recipesList = recipesList;
        notifyDataSetChanged();
    }

    public interface RecipeItemClickListener {
        void onRecipeItemClick(int position);
    }
}