package uk.ac.napier.androidcw2017_cook_app;

/**
 * Created by dtd2509 on 19/03/2017.
 */

public class Ingredient
{
    private String name;

    private int weight;

    public Ingredient(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName()
    {
        return name;
    }

    public int getWeight() {return weight;}
}
