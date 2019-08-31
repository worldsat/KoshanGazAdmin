package ir.koshangas.pasargad.product;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import ir.koshangas.pasargad.DateVolley;
import ir.koshangas.pasargad.OnLoadMoreListener;
import ir.koshangas.pasargad.R;


public class getProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<String> ImageItems, IdItems,ShowPrice, DescriptionItems, NameItems, SpecialItems, priceItems, Image1Items, CategoryItems;
    private List<String> DiscountItems, Image2Items, VotesItems,olaviat,PercentDiscountItems, Image3Items, Image4Items, Image5Items, Image6Items, OtherItems, AvaiableItems;


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;
    //----------------
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private Context context;


    private RecyclerView mRecyclerViewlist;
    private DecimalFormat formatter = new DecimalFormat("###,###,###,###");


    public getProductsAdapter(final Context context,List<String>ShowPrice,List<String> olaviat,List<String> DiscountItems, List<String> IdItems, List<String> DescriptionItems, List<String> NameItems, List<String> priceItems, List<String> ImageItems, List<String> Image1Items, List<String> Image2Items, List<String> VotesItems, List<String> Image3Items, List<String> Image4Items, List<String> Image5Items, List<String> Image6Items, List<String> OtherItems, List<String> CategoryItems, List<String> AvaiableItems, List<String> SpecialItems,List<String> PercentDiscountItems, RecyclerView recyclerViewlist) {

        this.DiscountItems = DiscountItems;
        this.IdItems = IdItems;
        this.DescriptionItems = DescriptionItems;
        this.NameItems = NameItems;
        this.priceItems = priceItems;
        this.ImageItems = ImageItems;
        this.VotesItems = VotesItems;
        this.Image1Items = Image1Items;
        this.Image2Items = Image2Items;
        this.Image3Items = Image3Items;
        this.Image4Items = Image4Items;
        this.Image5Items = Image5Items;
        this.Image6Items = Image6Items;
        this.OtherItems = OtherItems;
        this.mRecyclerViewlist = recyclerViewlist;
        this.context = context;
        this.CategoryItems = CategoryItems;
        this.AvaiableItems = AvaiableItems;
        this.SpecialItems = SpecialItems;
        this.ShowPrice = ShowPrice;
        this.olaviat = olaviat;
        this.PercentDiscountItems = PercentDiscountItems;

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

    @Override
    public int getItemViewType(int position) {

        return NameItems.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;

    }

    //--------------------------------------------------------MyViewHolder----------------------------------------------
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view;

            view = LayoutInflater.from(context).inflate(R.layout.itemview_layout, parent, false);

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

            myViewHolder.name.setText(changeNumber(NameItems.get(position)));
            myViewHolder.code.setText("کد دسته بندی میانی دوم: " + changeNumber(CategoryItems.get(position)));

            //hidden kardan code dar zirshakha

            myViewHolder.code.setVisibility(View.VISIBLE);


            // glide

            final String link = "http://www.koshangaspasargad.ir/koshangas/" + ImageItems.get(position);

            Glide.with(context)
                    .load(link)
                    .override(300, 300)
                    .error(R.mipmap.no_picture)
//                    .centerCrop()
                    .into(((MyViewHolder) holder).pic);
            //end glide


            myViewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {


                        Intent intent = new Intent(context, New_Product.class);
                        intent.putExtra("name", changeNumber(NameItems.get(position)));
                        intent.putExtra("id", IdItems.get(position));
                        intent.putExtra("description", DescriptionItems.get(position));
                        intent.putExtra("main_image", ImageItems.get(position));
                        intent.putExtra("category_id", CategoryItems.get(position));
                        intent.putExtra("isAvailable", AvaiableItems.get(position));
                        intent.putExtra("special", SpecialItems.get(position));
                        intent.putExtra("discount", DiscountItems.get(position));
                        intent.putExtra("price", priceItems.get(position));
                        intent.putExtra("MainImageItems", ImageItems.get(position));
                        intent.putExtra("votes", VotesItems.get(position));
                        intent.putExtra("ShowPrice", ShowPrice.get(position));
                        intent.putExtra("olaviat", olaviat.get(position));
                        intent.putExtra("percentDiscount", PercentDiscountItems.get(position));
                        //intent.putExtra("OtherImage", OtherItems.get(position).split(","));


                        SharedPreferences pic_writer = context.getSharedPreferences("picture", 0);
                        SharedPreferences.Editor edit = pic_writer.edit();
                        edit.clear();
                        edit.putString("Editable?", "yes");
                        edit.putString("id", IdItems.get(position));
                        edit.apply();

                        intent.putExtra("is_edit", "true");


                        context.startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(context, "خطایی پیش آمده است لطفا دوباره امتحان کنید", Toast.LENGTH_SHORT).show();
                        Log.i("mohsenjamali", "onClickError: " + e.toString());
                    }
                }
            });

            myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final MaterialDialog delete_alert = new MaterialDialog.Builder(context)
                            .customView(R.layout.alert_delete, false)
                            .autoDismiss(false)
                            .show();
                    TextView delete_alert_no = (TextView) delete_alert.findViewById(R.id.alert_delete_no);
                    TextView delete_alert_yes = (TextView) delete_alert.findViewById(R.id.alert_delete_yes);
                    TextView danger = (TextView) delete_alert.findViewById(R.id.danger);

                    danger.setVisibility(View.GONE);


                    delete_alert_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            delete_alert.dismiss();


                        }
                    });
                    delete_alert_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            delete_alert.dismiss();
                            DateVolley volley = new DateVolley();


                            volley.connect(context, "activity_product", "delete", false, "", "", "", IdItems.get(position),"");


                        }
                    });


                }
            });

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {

        return NameItems == null ? 0 : NameItems.size();
    }


    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        private LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        //bayad ajzaye layout ro tarif konim

        TextView name, edit, delete, code, code2;
        ImageView pic;

        // TextView card_txt = findViewById(R.id.card_basket_txt);
        private MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.title);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            code = itemView.findViewById(R.id.code);
            code2 = itemView.findViewById(R.id.code2);
            pic = itemView.findViewById(R.id.pic_show);

        }
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


}
