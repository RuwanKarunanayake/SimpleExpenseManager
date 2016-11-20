package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.AndroidDB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by ruwan on 11/20/16.
 */

public class PersistentTransactionDAO implements TransactionDAO{
    private SQLiteOpenHelper helper;

    public PersistentTransactionDAO(Context context){
        helper = new AndroidDB(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase sqldb = helper.getWritableDatabase();

        String query = String.format("INSERT OR IGNORE INTO TRANSACTION (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                Database.TransactionsTable.date,
                Database.TransactionsTable.accountNo,
                Database.TransactionsTable.expenseType,
                Database.TransactionsTable.amount);

        sqldb.execSQL(query, new Object[]{
                date.getTime(),
                accountNo,
                expenseType,
                amount});
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase sqldb = helper.getWritableDatabase();
        String query = String.format("SELECT %s, %s, %s, %s FROM TRANSACTION)",
                Database.TransactionsTable.date,
                Database.TransactionsTable.accountNo,
                Database.TransactionsTable.expenseType,
                Database.TransactionsTable.amount);

        List<Transaction> history = new ArrayList<>();

        final Cursor result = sqldb.rawQuery(query, null);

        if (result.moveToFirst()) {
            do {
                history.add(new Transaction(
                        new Date(result.getLong(0)),
                        result.getString(1),
                        Enum.valueOf(ExpenseType.class, result.getString(2)),
                        result.getDouble(3)));
            } while (result.moveToNext());
        }
        result.close();

        return history;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase sqldb = helper.getWritableDatabase();
        String query = String.format("SELECT %s, %s, %s, %s FROM TRANSACTION LIMIT %s)",
                Database.TransactionsTable.date,
                Database.TransactionsTable.accountNo,
                Database.TransactionsTable.expenseType,
                Database.TransactionsTable.amount,
                limit);

        List<Transaction> history = new ArrayList<>();

        final Cursor result = sqldb.rawQuery(query, null);

        if (result.moveToFirst()) {
            do {
                history.add(new Transaction(
                        new Date(result.getLong(0)),
                        result.getString(1),
                        Enum.valueOf(ExpenseType.class, result.getString(2)),
                        result.getDouble(3)));
            } while (result.moveToNext());
        }
        result.close();

        return history;
    }
}