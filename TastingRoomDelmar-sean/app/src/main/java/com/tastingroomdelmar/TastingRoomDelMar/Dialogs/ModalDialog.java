package com.tastingroomdelmar.TastingRoomDelMar.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.tastingroomdelmar.TastingRoomDelMar.ListViewAdapters.ModalListViewAdapter;
import com.tastingroomdelmar.TastingRoomDelMar.R;
import com.tastingroomdelmar.TastingRoomDelMar.parseUtils.ItemListObject;
import com.tastingroomdelmar.TastingRoomDelMar.parseUtils.ModalListItem;
import com.tastingroomdelmar.TastingRoomDelMar.utils.Constants;
import com.tastingroomdelmar.TastingRoomDelMar.utils.FontManager;
import com.tastingroomdelmar.TastingRoomDelMar.utils.OrderManager;

/**
 * Created by Sean on 4/14/16.
 */
public class ModalDialog extends Dialog implements android.view.View.OnClickListener {
    private static final String TAG = ModalDialog.class.getSimpleName();

    private AppCompatActivity mContext;

    private ItemListObject item;
    private boolean isEvent;

    private ArrayList<ModalListItem> modalItems;
    private ModalListViewAdapter adapter;
    private ListView modalListView;

    public ModalDialog(AppCompatActivity context, ItemListObject item, boolean isEvent) {
        super(context);
        mContext = context;
        this.item = item;
        this.isEvent = isEvent;

        modalItems = new ArrayList<>();

        adapter = new ModalListViewAdapter(context, modalItems);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_modal);

        if (FontManager.getSingleton() == null) new FontManager(mContext.getApplicationContext());

        final TextView name = (TextView) findViewById(R.id.modal_tv_name);
        final TextView info = (TextView) findViewById(R.id.modal_tv_info);
        final Button add = (Button) findViewById(R.id.modal_btn_add);
        final Button cancel = (Button) findViewById(R.id.modal_btn_cancel);

        name.setTypeface(FontManager.bebasReg);
        info.setTypeface(FontManager.openSansLight);
        add.setTypeface(FontManager.nexa);
        cancel.setTypeface(FontManager.nexa);

        name.setText(item.getName());
        info.setText(item.getAltName());

        add.setOnClickListener(this);
        cancel.setOnClickListener(this);

        modalListView = (ListView) findViewById(R.id.modal_listview);
        modalListView.setAdapter(adapter);

        if (!isEvent) {
            if (item.getServings() != null && !item.getServings().isEmpty()) {
                modalItems.add(new ModalListItem(Constants.Type.SERVING, Constants.SERVING, item.getServings()));
            }

            if (item.getAdditions() != null && !item.getAdditions().isEmpty()) {
                for (ModalListItem modalItem : item.getAdditions())
                    modalItems.add(modalItem);
            }

            modalItems.add(new ModalListItem());
        } else {
            modalItems.add(new ModalListItem(Constants.Type.SERVING, Constants.BOX_OFFICE, item.getServings()));
            modalItems.add(new ModalListItem());
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modal_btn_add:
                JSONObject topOrderObject = new JSONObject();

                JSONObject userObject = new JSONObject();
                JSONObject orderObject = new JSONObject();

                JSONObject dineinObject = new JSONObject();
                JSONObject takeawayObject = new JSONObject();

                JSONArray ordersArray = new JSONArray();

                JSONObject dineinOrderBody = new JSONObject();
                JSONObject takeawayOrderBody = new JSONObject();

                JSONArray dineinOrderItems = new JSONArray();
                JSONArray takeawayOrderItems = new JSONArray();

                JSONObject dineinOrderItemDetail = new JSONObject();
                JSONObject takeawayOrderItemDetail = new JSONObject();

                JSONArray dineinOrderItemDetailMod = new JSONArray();
                JSONArray takewayOrderItemDetailMod = new JSONArray();


                try {
                    /* put basic info to Dine-In and Takeaway */
                    dineinObject.put("checkoutMethod","stripe");
                    dineinObject.put("table","23");
                    dineinObject.put("tipPercent","0.152");

                    takeawayObject.put("checkoutMethod","stripe");
                    takeawayObject.put("table","23");
                    takeawayObject.put("tipPercent","0.151");

                    /* put basic info to order bodies */
                    dineinOrderBody.put("type","delivery");
                    dineinOrderBody.put("note","Tobe @ Table 23");

                    takeawayOrderBody.put("type","delivery");
                    takeawayOrderBody.put("note","Tobe @ Table 23");

                    /* add order item details to order item detail objects */
                    dineinOrderItemDetail.put("amount", "2");
                    dineinOrderItemDetail.put("objectId", "wUQAmfT9dQ");

                    takeawayOrderItemDetail.put("amount", "1");
                    takeawayOrderItemDetail.put("objectId", "wl5gmi1tq2");

                    /* add modifiers to details */
                    dineinOrderItemDetail.put("modifiers", dineinOrderItemDetailMod);
                    takeawayOrderItemDetail.put("modifiers", takewayOrderItemDetailMod);

                    /* add orderItemDetails to Order Items arrays */
                    dineinOrderItems.put(dineinOrderItemDetail);
                    takeawayOrderItems.put(takeawayOrderItemDetail);

                    /* add orderItems to order bodies */
                    dineinOrderBody.put("orderItems", dineinOrderItems);
                    takeawayOrderBody.put("orderItems", takeawayOrderItems);

                    /* add order body to Dine-In and Takeaway */
                    dineinObject.put("body",dineinOrderBody);
                    takeawayObject.put("body",takeawayOrderBody);

                    ordersArray.put(dineinObject);
                    ordersArray.put(takeawayObject);

                    // add structured item into orderObject
                    orderObject.put("userId", "zMoJ95Cew5");
                    orderObject.put("orders", ordersArray);

                    OrderManager orderManager = new OrderManager(orderObject);
                    orderManager.printOrder();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.modal_btn_cancel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}