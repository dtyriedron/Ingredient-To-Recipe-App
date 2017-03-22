package uk.ac.napier.androidcw2017_cook_app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static uk.ac.napier.androidcw2017_cook_app.MainActivity.recipes;

public class Output extends AppCompatActivity
{

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output);

        lv = (ListView) findViewById(R.id.list);

        ArrayAdapter<Recipe> adapter = new recipeArrayAdapter(this, 0, recipes);
        lv.setAdapter(adapter);

    }
    class recipeArrayAdapter extends ArrayAdapter<Recipe>
    {
        private Context context;
        private List<Recipe> recipeslist;

        public recipeArrayAdapter(Context context, int resource, ArrayList<Recipe> objects)
        {
            super(context,resource,objects);
            this.context = context;
            this.recipeslist= objects;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            Recipe recipe = recipeslist.get(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.listitem,null);

            TextView title = (TextView) view.findViewById(R.id.title);
            TextView ingredients = (TextView) view.findViewById(R.id.ingredients);
            WebView image = (WebView) view.findViewById(R.id.image);

            title.setText(recipe.getTitle());
            String all="";

            for(Ingredient i: recipe.getIngredients())
                all+= "\n" +i.getName();

            ingredients.setText(all);

            image.loadUrl(recipe.getImage());
            return view;

        }
    }
}
