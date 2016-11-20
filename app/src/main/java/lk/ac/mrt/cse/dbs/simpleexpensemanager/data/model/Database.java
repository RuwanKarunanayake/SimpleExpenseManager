package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;

import android.provider.BaseColumns;

/**
 * Created by ruwan on 11/20/16.
 */

public class Database {
    static final String dbName = "140287V";

    public static class AccountsTable implements BaseColumns {
        public static final String accountNo = "accountNo";
        public static final String bankName = "bankName";
        public static final String accountHolderName = "accountHolderName";
        public static final String balance = "balance";
    }

    public static class TransactionsTable implements BaseColumns{
        public static final String date = "date";
        public static final String accountNo = "accountNo";
        public static final String expenseType = "expenseType";
        public static final String amount = "amount";
    }
}
