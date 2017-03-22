package uk.ac.napier.androidcw2017_cook_app;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by dtd2509 on 19/03/2017.
 */

public class Recipe extends ArrayList<View> {
    private String title;
    private String image;
    private ArrayList<Ingredient> ingredients;

    public Recipe(String title, String image) {
        this.title = title;
        this.image = image;
        ingredients = new ArrayList<>();
    }

    public void addIngredient(Ingredient ingredient )
    {
        this.ingredients.add(ingredient);
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }
}
