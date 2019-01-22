package ir.koshangas.pasargad.basket;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import ir.koshangas.pasargad.OnLoadMoreListener;
import ir.koshangas.pasargad.R;


public class getBasketItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<String> IdItems, DateItems, PriceItems, UserItems, NameItems, ProductIdItems, QtyItems, FactoreItems, TotalPriceIdItems, PaymentMethodItems, SendMethodItems, StatusItems;
    private TextView emptyText;
    private OnLoadMoreListener mOnLoadMoreListener;
    //----------------
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private Context context;

    private RecyclerView mRecyclerViewlist;
    private DecimalFormat formatter = new DecimalFormat("###,###,###,###");


    public getBasketItemAdapter(final Context context, List<String> IdItems, List<String> DateItems, List<String> PriceItems, List<String> UserItems, List<String> NameItems, List<String> ProductIdItems, List<String> QtyItems, List<String> FactoreItems, List<String> TotalPriceIdItems, List<String> PaymentMethodItems, List<String> SendMethodItems, List<String> StatusItems, TextView emptyText, RecyclerView recyclerViewlist) {
        this.context = context;
        this.IdItems = IdItems;
        this.DateItems = DateItems;
        this.PriceItems = PriceItems;
        this.mRecyclerViewlist = recyclerViewlist;
        this.emptyText = emptyText;
        this.UserItems = UserItems;
        this.NameItems = NameItems;
        this.ProductIdItems = ProductIdItems;
        this.QtyItems = QtyItems;
        this.FactoreItems = FactoreItems;
        this.TotalPriceIdItems = TotalPriceIdItems;
        this.PaymentMethodItems = PaymentMethodItems;
        this.SendMethodItems = SendMethodItems;
        this.StatusItems = StatusItems;


//---------------------------
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerViewlist.getLayoutManager();
        mRecyclerViewlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });

    }

    private static boolean isEven(int number) {
        return (number % 2 == 0);
    }

    @Override
    public int getItemViewType(int position) {
        return NameItems.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    //--------------------------------------------------------MyViewHolder----------------------------------------------
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view;

            view = LayoutInflater.from(context).inflate(R.layout.itemview_layout_basket_item, parent, false);

            MyViewHolder vh = new MyViewHolder(view);
            return vh;
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.loading_layout, parent, false);
            LoadingViewHolder vh2 = new LoadingViewHolder(view);
            return vh2;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder) {
            final MyViewHolder myViewHolder = (MyViewHolder) holder;

            myViewHolder.Name.setText(changeNumber(NameItems.get(position)));
            String str1 = changeNumber(formatter.format(Long.valueOf(PriceItems.get(position))) + " تومان");
            myViewHolder.Price.setText(str1);

            String str2 =  changeNumber(formatter.format(Long.valueOf(QtyItems.get(position))));
            myViewHolder.Factor.setText(str2);
//            //Hide line for last record
//            if ((myViewHolder.getAdapterPosition() + 1) == IdItems.size()) {
//                myViewHolder.Line.setVisibility(View.GONE);
//
//            }

            if (isEven(myViewHolder.getLayoutPosition())) {
                myViewHolder.cardview.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            } else {
                myViewHolder.cardview.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
            }


        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return NameItems == null ? 0 : NameItems.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void setLoaded() {
        isLoading = false;
    }

    private String changeNumber(String num) {
        num = num.replaceAll("0", "۰");
        num = num.replaceAll("1", "۱");
        num = num.replaceAll("2", "۲");
        num = num.replaceAll("3", "۳");
        num = num.replaceAll("4", "۴");
        num = num.replaceAll("5", "۵");
        num = num.replaceAll("6", "۶");
        num = num.replaceAll("7", "۷");
        num = num.replaceAll("8", "۸");
        num = num.replaceAll("9", "۹");
        return num;
    }

    private String changeNumberToEN(String num) {
        num = num.replaceAll("۰", "0");
        num = num.replaceAll("۱", "1");
        num = num.replaceAll("۲", "2");
        num = num.replaceAll("۳", "3");
        num = num.replaceAll("۴", "4");
        num = num.replaceAll("۵", "5");
        num = num.replaceAll("۶", "6");
        num = num.replaceAll("۷", "7");
        num = num.replaceAll("۸", "8");
        num = num.replaceAll("۹", "9");
        return num;
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        private LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Name, Price, Factor;
        // View Line;
        LinearLayout cardview;

        private MyViewHolder(View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.titleNews);
            Price = itemView.findViewById(R.id.Price_show);
            Factor = itemView.findViewById(R.id.Factor_show);
            //  Line = itemView.findViewById(R.id.Line);
            cardview = itemView.findViewById(R.id.cardview);

        }

    }


}
