package com.tsysinfo.oneabove.database;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DataBaseAdapter {
    private static final String DATABASE_NAME = "ezeeclub";
    private static final int DATABASE_VERSION = 3;
    public static SQLiteDatabase ourDatabase;
    private final Context ourContext;
    private DBHelper ourHelper;
    public DataBaseAdapter(Context c) {
        ourContext = c;
    }
    public DataBaseAdapter open() throws SQLException {
        ourHelper = new DBHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        ourHelper.close();
    }
    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL(Mst_User.CREATE_TABLE);
            db.execSQL(DietTable.CREATE_TABLE);
            db.execSQL(NotificationTable.CREATE_TABLE);
            db.execSQL(MessageTable.CREATE_TABLE);
            db.execSQL(WorkoutTable.CREATE_TABLE);
			/*db.execSQL(MemberTable.CREATE_TABLE);
			db.execSQL(ReceiptTable.CREATE_TABLE);
			db.execSQL(PlanDetailsTable.CREATE_TABLE);
			db.execSQL(CounsellerTable.CREATE_TABLE);
			db.execSQL(BranchTable.CREATE_TABLE);
			db.execSQL(settingTable.CREATE_TABLE);
			db.execSQL(ConversionRatioTable.CREATE_TABLE);
			db.execSQL(ReceiptIncomeTable.CREATE_TABLE);
			db.execSQL(PlanSaleTable.CREATE_TABLE);
			db.execSQL(DailyStatusTable.CREATE_TABLE);
			db.execSQL(BranchDetailsTable.CREATE_TABLE);
			db.execSQL(MessagesTable.CREATE_TABLE);
			db.execSQL(BranchChekTable.CREATE_TABLE);
			db.execSQL(MessageUserTable.CREATE_TABLE);
			db.execSQL(MemberDetailsTable.CREATE_TABLE);
			db.execSQL(QuickSaleMemberTable.CREATE_TABLE);
			db.execSQL(CounttryCodeTable.CREATE_TABLE);
			db.execSQL(IPTable.CREATE_TABLE);
			db.execSQL(ItemTable.CREATE_TABLE);
			db.execSQL(ItemSaleTable.CREATE_TABLE);
			db.execSQL(RenewPlanTable.CREATE_TABLE);
			db.execSQL(OutstandingPaymentTable.CREATE_TABLE);
			db.execSQL(AuthorityTable.CREATE_TABLE);
			db.execSQL(TempPlanSale.CREATE_TABLE);
			db.execSQL(TempReceipt.CREATE_TABLE);
			db.execSQL(R2Table.CREATE_TABLE);
			db.execSQL(EnquiryToMemberTable.CREATE_TABLE);
			db.execSQL(OfflinePayment.CREATE_TABLE);
			db.execSQL(DieticianTable.CREATE_TABLE);
            db.execSQL(DietTable.CREATE_TABLE);
			db.execSQL(DietTemplateTable.CREATE_TABLE);
			db.execSQL(EnquiryMemberDetailsTable.CREATE_TABLE);
            db.execSQL(FollowUpTagTable.CREATE_TABLE);
			db.execSQL(StatusFollowUpTable.CREATE_TABLE);
			db.execSQL(TempImage.CREATE_TABLE);
			db.execSQL(PayModeTable.CREATE_TABLE);
			db.execSQL(EnquiryType.CREATE_TABLE);
			db.execSQL(DietHistData.CREATE_TABLE);*/
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub

            db.execSQL("DROP TABLE IF EXISTS " + Mst_User.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DietTable.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + NotificationTable.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "  +MessageTable.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "  +WorkoutTable.DATABASE_TABLE);

			/*db.execSQL("DROP TABLE IF EXISTS " + MemberTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + ReceiptTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + PlanDetailsTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CounsellerTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + BranchTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + settingTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + ConversionRatioTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + ReceiptIncomeTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + PlanSaleTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DailyStatusTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + BranchDetailsTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + MessagesTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + BranchChekTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + MessageUserTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + MemberDetailsTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + QuickSaleMemberTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CounttryCodeTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + IPTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + ItemTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + ItemSaleTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + RenewPlanTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + OutstandingPaymentTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + AuthorityTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + TempPlanSale.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + TempReceipt.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + R2Table.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + EnquiryToMemberTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + OfflinePayment.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DieticianTable.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DietTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DietTemplateTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + EnquiryMemberDetailsTable.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + FollowUpTagTable.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + StatusFollowUpTable.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + TempImage.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + PayModeTable.DATABASE_TABLE);
 			db.execSQL("DROP TABLE IF EXISTS " + EnquiryType.DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + DietHistData.DATABASE_TABLE);*/
            onCreate(db);
        }
    }
}
