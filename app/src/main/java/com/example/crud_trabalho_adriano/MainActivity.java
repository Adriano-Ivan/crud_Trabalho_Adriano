package com.example.crud_trabalho_adriano;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, PopupMenu.OnMenuItemClickListener,
        DialogToSaveItem.onCancelListener, DialogToSaveItem.onSaveListener{

    ListView lista_view;
    ArrayAdapter adapter;
    String [] examples = {};
    List<String> listForAdapter = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MenuItem menuItem = (MenuItem) findViewById(R.id.context_add);

        lista_view = (ListView) findViewById(R.id.lista);
        addExamplesToList();

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                listForAdapter);


        lista_view.setAdapter(adapter);

    }

    public void openSaveItemDialog(){
        DialogToSaveItem dialog = new DialogToSaveItem();
        dialog.show(getSupportFragmentManager(),"dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        View imgAux = (View) findViewById(R.id.auxImg);
        PopupMenu popupMenu = new PopupMenu(this, imgAux);
        popupMenu.inflate(R.menu.context_menu);
        popupMenu.setOnMenuItemClickListener(this);

        getMenuInflater().inflate(R.menu.context_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.context_add)
        {
//            Toast.makeText(this, R.string.adicionar, Toast.LENGTH_SHORT).show();
            openSaveItemDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addExamplesToList(){
        for(String example: examples){
            listForAdapter.add(example);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        if(menuItem.getItemId() == R.id.context_add){
            Toast.makeText(this, R.string.adicionar, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.context_add){
            Toast.makeText(this, R.string.adicionar, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onCancel(FragmentActivity activity,int mensagem) {
        Toast.makeText(activity, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSave(FragmentActivity activity,String nameItem, int mensagem) {
        Toast.makeText(activity, mensagem, Toast.LENGTH_SHORT).show();
        listForAdapter.add(nameItem);
        lista_view.setAdapter(adapter);
    }
}