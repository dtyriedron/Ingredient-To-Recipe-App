package uk.ac.napier.androidcw2017_cook_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;

    public static Boolean isclicked =false;

    public static String allingredients = "";

    EditText AddIngredText;
    TextView IngredsView;


    // URL to get contacts JSON

    public static ArrayList<HashMap<String, String>> ingredientList;
    public static ArrayList<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AddIngredText = (EditText) findViewById(R.id.AddIngredText);
        IngredsView = (TextView) findViewById(R.id.IngredsView);


        Button Add_Button = (Button) findViewById(R.id.Add_Button);
        Add_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                //add to textview if first ingredient then search the database

                if(!AddIngredText.getText().equals(""))
                {
                    IngredsView.append(AddIngredText.getText().toString()+",\n");
                    AddIngredText.setText("");
                    AddIngredText.setHint("Enter your Next Ingredient");
                    new GetIngredients().execute();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "you need to enter an ingredient, then press the add ingredient button!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button Search_Button = (Button) findViewById(R.id.Search_Button);
        Search_Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                isclicked=true;
                new GetIngredients().execute();
                String[] separated = IngredsView.getText().toString().split(",");


                if(!IngredsView.getText().equals(""))
                {

                    //ask if veggie?
                    for(Recipe r: recipes)
                    {
                        for(Ingredient i: r.getIngredients())
                        {
                            for (int k=0;k<IngredsView.getLineCount();k++)
                            {
                                if (i.getName().equals(separated[k]))
                                {
                                    allingredients+= i.getName();
                                }
                                allingredients+=",";
                            }

                        }
                    }
                }
                else
                {
                    Toast.makeText(getBaseContext(), "you have no ingredients... please add using the add ingredient button", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ingredientList = new ArrayList<>();



        recipes = new ArrayList<>();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetIngredients extends AsyncTask<Void, Void, Void>
    {

        public static final String API_KEY = "025a1db8&app_key=f0823e727e12b09fcfdb8d5fd552148e";
        public static final String API_URL = "https://api.edamam.com/search?";

        String ingredient = AddIngredText.getText().toString();
        String allingredients = IngredsView.getText().toString();
        String fullurl = (API_URL + "q="+ingredient+"&app_id=" + API_KEY);


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            if (isclicked==true)
            {
                fullurl=(API_URL+ "q="+allingredients+"&app_id="+API_KEY);
            }
            else
            {
                fullurl=(API_URL + "q="+ingredient+"&app_id=" + API_KEY);
            }
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(fullurl);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null)
            {
                try
                {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray hitsArray = jsonObj.getJSONArray("hits");

                    for (int i=0;i<hitsArray.length();i++)
                    {
                        JSONObject hitsOBJ = hitsArray.getJSONObject(i);
                        JSONObject recipe = hitsOBJ.getJSONObject("recipe");
                        String image = recipe.getString("image");
                        JSONArray ingredientsArray = recipe.getJSONArray("ingredients");
                        String name = recipe.getString("label");


                        Recipe r = new Recipe(name,image);


                        // looping through All ingredients
                        for (int j = 0; j < ingredientsArray.length(); j++)
                        {
                            JSONObject ingredientsOBJ = ingredientsArray.getJSONObject(j);

                            String text =  ingredientsOBJ.getString("text");
                            int weight =  ingredientsOBJ.getInt("weight");

                            // adding each child node to HashMap key => value
                            r.addIngredient(new Ingredient(text, weight));
                        }
                        recipes.add(r);

                    }

                }
                catch (final JSONException e)
                {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            }
            else
            {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            //if allingredients then go ahead else go back to the search button
            if(isclicked==true)
            {
                Intent intent = new Intent(MainActivity.this, Output.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);

            }else
            {
                Toast.makeText(getBaseContext(), "add another ingredient or search for recipes!!", Toast.LENGTH_SHORT).show();
            }
        }

    }



}
