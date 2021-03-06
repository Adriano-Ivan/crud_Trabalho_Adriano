package com.example.crud_trabalho_adriano;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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
        DialogToSaveItem.onCancelListener, DialogToSaveItem.onEditListener,
        DialogToSaveItem.onSaveListener, AbsListView.MultiChoiceModeListener {

    private ListView lista_view;
    private ArrayAdapter<String> adapter;
    private String [] examples = {};
    private List<String> listForAdapter = new ArrayList<>();
    private List<String> selecionados = new ArrayList<>();
    private View menuItemAdd;
    private View menuItemDelete ;
    private View menuItemEdit;
    private boolean mShowAddVisible=true;
    private boolean mShowEditVisible = true;
    private boolean mShowDeleteVisible = true;
    private int idItemMenu;
    private String valueToEditItem;
    private int positionToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista_view =  findViewById(R.id.lista);

        addExamplesToList();
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                listForAdapter
                );


        lista_view.setAdapter(adapter);


        lista_view.setMultiChoiceModeListener(this);
        lista_view.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
    }

    public void openSaveCreatedItemDialog(){
        DialogToSaveItem dialog = new DialogToSaveItem(OPERACAO.INSERT);
        dialog.show(getSupportFragmentManager(),"dialog");
    }

    public void openSaveUpdatedItemDialog(){
        DialogToSaveItem dialog = new DialogToSaveItem(OPERACAO.EDIT,valueToEditItem);
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
            openSaveCreatedItemDialog();
        } else if(item.getItemId() == R.id.context_edit) {
            openSaveUpdatedItemDialog();
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
        } else if(menuItem.getItemId() == R.id.context_edit){
            Toast.makeText(this, R.string.editar, Toast.LENGTH_SHORT).show();
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
        if(alreadyExists(nameItem)){
            Toast.makeText(activity, R.string.already_exists, Toast.LENGTH_SHORT).show();
        }
        else if(nameItem.trim().length() > 0){
            Toast.makeText(activity, mensagem, Toast.LENGTH_SHORT).show();
            listForAdapter.add(nameItem);
            lista_view.setAdapter(adapter);
//            adapter.add(nameItem);
//            lista_view.setAdapter(adapter);
        }

    }

    private boolean alreadyExists(String nameItem){
        boolean alreadyExists = false;

        for(String elementOfAdap: listForAdapter){
            if(elementOfAdap.trim().toLowerCase().equals(nameItem.trim().toLowerCase()) ){
                alreadyExists = true;
                break;
            }
        }

        return alreadyExists;
    }
    @Override
    public void onEdit(FragmentActivity activity,String nameItem, int mensagem) {
       if(alreadyExists(nameItem)){
           Toast.makeText(activity, R.string.already_exists, Toast.LENGTH_SHORT).show();
       }
       else if(nameItem.trim().length() > 0){
            Toast.makeText(activity, mensagem, Toast.LENGTH_SHORT).show();
            listForAdapter.set(positionToEdit, nameItem);

            lista_view.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        getMenuInflater().inflate(R.menu.context_menu,menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        menu.findItem(R.id.context_add).setVisible(mShowAddVisible);
        menu.findItem(R.id.context_edit).setVisible(mShowEditVisible);
        menu.findItem(R.id.context_delete).setVisible(mShowDeleteVisible);

        if(mShowDeleteVisible && mShowEditVisible){
            menu.findItem(R.id.context_add).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if(item.getItemId() == R.id.context_delete){
            for(String s:selecionados){
                listForAdapter.remove(s);
//                adapter.remove(s);
            }
            lista_view.setAdapter(adapter);
            mode.finish();
            return true;
        } else if (item.getItemId() == R.id.context_edit){
            openSaveUpdatedItemDialog();
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        int count = lista_view.getChildCount();

        selecionados = new ArrayList<String>();

        for(int i = 0; i < count; i++){
            View view = lista_view.getChildAt(i);
            view.setBackgroundColor(Color.TRANSPARENT);
        }

    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        String selecionado = listForAdapter.get(position);
        View view = lista_view.getChildAt(position);

        valueToEditItem = selecionado;
        positionToEdit = position;

        if(!selecionados.contains(selecionado)){
            view.setBackgroundColor(Color.parseColor("#0022FF"));
            selecionados.add(selecionado);

            mShowAddVisible = false;

            if(selecionados.size() == 1){
                mShowEditVisible = true;

                mShowDeleteVisible = true;
                invalidateOptionsMenu();
            } else {
//                menuItemDelete.setVisibility(View.VISIBLE);
                mShowDeleteVisible = true;

                mShowEditVisible = true;
                invalidateOptionsMenu();
//                menuItemEdit.setVisibility(View.INVISIBLE);
            }
        } else {
            view.setBackgroundColor(Color.TRANSPARENT);

            selecionados.remove(selecionado);

            if(selecionados.size() == 0){
                mShowAddVisible = false;

                mShowEditVisible = true;

                mShowDeleteVisible = true;
                invalidateOptionsMenu();
                mode.finish();
            } else {

            }
        }
    }

    public enum OPERACAO {
        INSERT ,
        EDIT,
    }
}