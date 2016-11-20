package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ruwan on 11/20/16.
 */

    public class AndroidDB extends SQLiteOpenHelper{
        static int database_version = 1;

        public AndroidDB(Context context){
            super(context, Database.dbName, null, database_version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE ACCOUNT (" + Database.AccountsTable.accountNo + " TEXT PRIMARY KEY, "
                    + Database.AccountsTable.bankName + " TEXT" + Database.AccountsTable.accountHolderName + " TEXT"
                    + Database.AccountsTable.balance + " REAL" + " )");
            db.execSQL("CREATE TABLE TRANSACTION (" + Database.TransactionsTable.date + " INT PRIMARY KEY, "
                    + "FOREIGN KEY (" + Database.TransactionsTable.accountNo + ") REFERENCES "
                    + Database.TransactionsTable.expenseType + " TEXT"
                    + Database.TransactionsTable.amount + " REAL" + ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS ACCOUNT");
            db.execSQL("DROP TABLE IF EXISTS TRANSACTION");
            onCreate(db);
        }


}
