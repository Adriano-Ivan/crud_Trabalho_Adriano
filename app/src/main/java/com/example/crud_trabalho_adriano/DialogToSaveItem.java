package com.example.crud_trabalho_adriano;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class DialogToSaveItem extends DialogFragment implements DialogInterface.OnClickListener {

    private onSaveListener saveListener;
    private onCancelListener cancelListener;
    private TextView inputItem;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_teste_message)
                .setPositiveButton(R.string.ok,this)
                .setNegativeButton(R.string.cancel,this);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_input,null);

        builder.setView(layout);

        inputItem = layout.findViewById(R.id.input_item);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == dialog.BUTTON_POSITIVE){
            String conteudo = String.valueOf(inputItem.getText());
            saveListener.onSave(getActivity(), conteudo, R.string.sera_salvo);
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
        }
        else {
            throw new RuntimeException("A activity deve implementar as interfaces.");
        }
    }

    public interface onCancelListener{
        void onCancel(FragmentActivity activity, int mensagem);
    }

    public interface onSaveListener{
        void onSave(FragmentActivity activity, String conteudo,int mensagem);
    }
}
