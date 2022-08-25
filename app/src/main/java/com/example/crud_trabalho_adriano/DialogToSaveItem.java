package com.example.crud_trabalho_adriano;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.crud_trabalho_adriano.data.Tarefa;

public class DialogToSaveItem extends DialogFragment implements DialogInterface.OnClickListener {

    private onSaveListener saveListener;
    private onCancelListener cancelListener;
    private onEditListener editListener;
    private TextView inputItem;
    private MainActivity.OPERACAO type;
    private Tarefa valueFieldIfIsToEdit;
    private int idIfIsToEdit;

    public DialogToSaveItem(MainActivity.OPERACAO type){
        this.type = type;
    }

    public DialogToSaveItem(MainActivity.OPERACAO type, Tarefa valueFieldIfIsToEdit){
        this.type = type;
        this.valueFieldIfIsToEdit = valueFieldIfIsToEdit;
        this.idIfIsToEdit  = valueFieldIfIsToEdit.getId();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(type == MainActivity.OPERACAO.INSERT){
            builder.setTitle(R.string.dialog_insert_title);
        }

        if(type == MainActivity.OPERACAO.EDIT){
            builder.setTitle(R.string.dialog_edit_title);
        }

        builder.setPositiveButton(R.string.ok,this)
                .setNegativeButton(R.string.cancel,this);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_input,null);

        builder.setView(layout);

        inputItem = layout.findViewById(R.id.input_item);
        inputItem.setBackgroundColor(Color.parseColor("#EEEEEE"));

        if(valueFieldIfIsToEdit != null){
            inputItem.setText(valueFieldIfIsToEdit.getTexto());
        }

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == dialog.BUTTON_POSITIVE && valueFieldIfIsToEdit == null){
            String conteudo = String.valueOf(inputItem.getText());
            Tarefa tarefa = new Tarefa(conteudo);
            saveListener.onSave(getActivity(), tarefa, R.string.sera_salvo);
        }
        else if(which == dialog.BUTTON_POSITIVE && valueFieldIfIsToEdit != null){
            String conteudo = String.valueOf(inputItem.getText());
            Tarefa tarefa = new Tarefa(conteudo);
            editListener.onEdit(getActivity(),tarefa,idIfIsToEdit, R.string.sera_editado);
        }
        else if(which == dialog.BUTTON_NEGATIVE){
            cancelListener.onCancel(getActivity(),R.string.nao_sera_salvo);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if(context instanceof onCancelListener
                && context instanceof onSaveListener){
            saveListener = (onSaveListener) context;
            cancelListener = (onCancelListener) context;
            editListener = (onEditListener) context;
        }
        else {
            throw new RuntimeException("A activity deve implementar as interfaces.");
        }
    }

    public interface onCancelListener{
        void onCancel(FragmentActivity activity, int mensagem);
    }

    public interface onSaveListener{
        void onSave(FragmentActivity activity, Tarefa conteudo,int mensagem);
    }

    public interface onEditListener{
        void onEdit(FragmentActivity activity, Tarefa conteudo,int id, int mensagem);
    }
}
