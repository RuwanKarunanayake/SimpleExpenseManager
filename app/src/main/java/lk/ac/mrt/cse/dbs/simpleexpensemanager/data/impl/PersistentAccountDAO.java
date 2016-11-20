package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
//DWDIWNDWDWDWDWDWD
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.AndroidDB;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
/**
 * Created by ruwan on 11/20/16.
 */

public class PersistentAccountDAO implements AccountDAO{
    private SQLiteOpenHelper helper;

    public PersistentAccountDAO(Context context){
        helper = new AndroidDB(context);
    }


    @Override
    public List<String> getAccountNumbersList() {
        SQLiteDatabase sqldb = helper.getReadableDatabase();

        String query = String.format("SELECT %s from ACCOUNT",
                Database.AccountsTable.accountNo);

        List<String> numbers = new ArrayList<>();

        final Cursor result = sqldb.rawQuery(query, null);

        if(result.moveToFirst()){
            do{
                numbers.add(result.getString(0));
            } while(result.moveToNext());
        }
        result.close();
        return numbers;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase sqldb = helper.getReadableDatabase();

        String query = String.format("SELECT %s %s %s %s FROM ACCOUNT",
                Database.AccountsTable.accountNo,
                Database.AccountsTable.bankName,
                Database.AccountsTable.accountHolderName,
                Database.AccountsTable.balance);

        List<Account> accounts = new ArrayList<>();

        final Cursor result = sqldb.rawQuery(query, null);

        if(result.moveToFirst()){
            do{
                accounts.add(new Account(result.getString(0), result.getString(1), result.getString(2), result.getDouble(3)));
            }while(result.moveToNext());
        }
        result.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase sqldb = helper.getReadableDatabase();

        String query = String.format("SELECT %s %s %s %s from ACCOUNT WHERE %s = ?",
                Database.AccountsTable.accountNo,
                Database.AccountsTable.bankName,
                Database.AccountsTable.accountHolderName,
                Database.AccountsTable.balance,
                Database.AccountsTable.accountNo);

        final Cursor result = sqldb.rawQuery(query, null);

        if(!result.moveToFirst()){
            throw new InvalidAccountException("Account " + accountNo + " is invalid");
        }

        Account acc = new Account(result.getString(0), result.getString(1), result.getString(2), result.getDouble(3));
        result.close();
        return acc;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase sqldb = helper.getWritableDatabase();

        String query = String.format("INSERT OR IGNORE INTO ACCOUNT (%s %s %s %s) VALUES ? ? ? ?",
                Database.AccountsTable.accountNo,
                Database.AccountsTable.bankName,
                Database.AccountsTable.accountHolderName,
                Database.AccountsTable.balance);

        sqldb.execSQL(query, new Object[]{
                account.getAccountNo(),
                account.getBankName(),
                account.getAccountHolderName(),
                account.getBalance()});

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        getAccount(accountNo);

        SQLiteDatabase sqldb = helper.getWritableDatabase();

        String query = String.format("DELETE FROM ACCOUNT WHERE %S = ?",
                Database.AccountsTable.accountNo);

        sqldb.execSQL(query, new Object[]{accountNo});
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        getAccount(accountNo);

        SQLiteDatabase sqldb = helper.getWritableDatabase();

        String query = null;
        switch (expenseType){
            case EXPENSE: query = "UPDATE ACCOUNT SET %s = %s - ? WHERE %s = ?";
            case INCOME: query = "UPDATE ACCOUNT SET %s = %s + ? WHERE %s = ?";
        }

        query = String.format(query,
                Database.AccountsTable.balance,
                Database.AccountsTable.balance,
                Database.AccountsTable.accountNo);
        sqldb.execSQL(query, new Object[]{accountNo, amount});
    }
}