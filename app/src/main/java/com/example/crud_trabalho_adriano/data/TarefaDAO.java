package com.example.crud_trabalho_adriano.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {
    private static TarefaDAO instance;

    private SQLiteDatabase db;

    private TarefaDAO(Context context) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    //singleton
    public static TarefaDAO getInstance(Context context) {
        if (instance == null) {
            instance = new TarefaDAO(context.getApplicationContext());
        }
        return instance;
    }

    public List<Tarefa> list() {

        String[] columns = {
                TarefasContract.Columns._ID,
                TarefasContract.Columns.TEXTO
        };

        List<Tarefa> tarefas = new ArrayList<>();

        try (Cursor c = db.query(TarefasContract.TABLE_NAME, columns, null, null, null, null, TarefasContract.Columns.TEXTO)) {
            if (c.moveToFirst()) {
                do {
                    Tarefa t = TarefaDAO.fromCursor(c);
                    tarefas.add(t);
                } while (c.moveToNext());
            }

            return tarefas;
        }

    }

    private static Tarefa fromCursor(Cursor c) {
        @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(TarefasContract.Columns._ID));
        @SuppressLint("Range") String texto = c.getString(c.getColumnIndex(TarefasContract.Columns.TEXTO));

        return new Tarefa(id, texto);
    }

    public void save(Tarefa tarefa) {
        ContentValues values = new ContentValues();
        values.put(TarefasContract.Columns.TEXTO, tarefa.getTexto());

        long id = db.insert(TarefasContract.TABLE_NAME, null, values);
        tarefa.setId((int) id);
    }

    public void update(Tarefa tarefa) {
        ContentValues values = new ContentValues();
        values.put(TarefasContract.Columns.TEXTO, tarefa.getTexto());

        db.update(TarefasContract.TABLE_NAME, values, TarefasContract.Columns._ID + " = ?", new String[]{ String.valueOf(tarefa.getId()) });
    }

    public void delete(Tarefa tarefa) {
        db.delete(TarefasContract.TABLE_NAME, TarefasContract.Columns._ID + " = ?", new String[]{ String.valueOf(tarefa.getId()) });
    }
}
