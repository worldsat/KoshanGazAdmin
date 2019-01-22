package ir.koshangas.pasargad;

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

import ir.koshangas.pasargad.category.New_Category;
import ir.koshangas.pasargad.middleCategory.New_MiddleCategory;
import ir.koshangas.pasargad.middleCategory2.New_MiddleCategory2;
import ir.koshangas.pasargad.product.New_Product;


public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //consteraktor ra misazim

    private List<String> ImageItems, NameItems, AvaiableItems, ParentItems;

    private List<String> IdItems, DescriptionItems, CategoryItems;


    //neshan dahandehe noe layout
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    //tarife interface
    private OnLoadMoreListener mOnLoadMoreListener;
    //----------------
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private Context context;
    private String Mode;
    private RecyclerView mRecyclerViewlist;
    private DecimalFormat formatter = new DecimalFormat("###,###,###,###");
    private SharedPreferences list_food_number;


    public RVAdapter(final Context context, String Mode, List<String> NameItems, List<String> IdItems, List<String> ParentItems, List<String> ImageItems, List<String> DescriptionItems, List<String> CategoryItems, List<String> AvailableItem, RecyclerView recyclerViewlist) {
        this.context = context;

        this.ImageItems = ImageItems;
        this.NameItems = NameItems;
        this.DescriptionItems = DescriptionItems;
        this.IdItems = IdItems;
        this.CategoryItems = CategoryItems;
        this.AvaiableItems = AvailableItem;
        this.ParentItems = ParentItems;
        this.Mode = Mode;
        this.mRecyclerViewlist = recyclerViewlist;


//------------moshakhas kardane inke aya be entehaye list resideheim ya na---------------
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
//------------------------------------------------------------

    }

    @Override
    public int getItemViewType(int position) {
//moshakas kardane layout nemayesh-----viewtype
        return IdItems.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;

    }

    //--------------------------------------------------------MyViewHolder----------------------------------------------
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//----------taein noe holder ba tavajoh be viewtype
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.itemview_layout, parent, false);
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
        //itemhaei ke bayad neshan dadeh shavad bayad inja vared shavad
        //check kardane inke kodam halate nemayesh darhale ejrast
        if (holder instanceof MyViewHolder) {
            final MyViewHolder myViewHolder = (MyViewHolder) holder;

            myViewHolder.name.setText(changeNumber(NameItems.get(position)));
            //  myViewHolder.code.setText("کد دسته بندی: " + changeNumber(IdItems.get(position)));

            //hidden kardan code dar zirshakha
            if (Mode.equals("activity_product")) {
                myViewHolder.code.setVisibility(View.INVISIBLE);
            }
            if (Mode.equals("middle_category")) {
                myViewHolder.code.setText("کد دسته بندی میانی اول: " + changeNumber(IdItems.get(position)));
                myViewHolder.code2.setVisibility(View.VISIBLE);
                myViewHolder.code2.setText("کد دسته بندی اصلی: " + changeNumber(ParentItems.get(position)));

            } else if (Mode.equals("middle_category2")) {
                myViewHolder.code.setVisibility(View.VISIBLE);
                myViewHolder.code2.setVisibility(View.VISIBLE);
                myViewHolder.code.setText("کد دسته بندی میانی اول: " + changeNumber(ParentItems.get(position)));
                myViewHolder.code2.setText("کد دسته بندی میانی دوم: " + changeNumber(IdItems.get(position)));

            } else {
                myViewHolder.code.setText("کد دسته بندی اصلی: " + changeNumber(IdItems.get(position)));
                myViewHolder.code2.setVisibility(View.INVISIBLE);
            }
            // glide

            final String link = "http://www.koshangaspasargad.ir/koshangas/" + ImageItems.get(position);

            Glide.with(context)
                    .load(link)
                    .override(300, 300)
                    .error(R.mipmap.no_picture)
                    .centerCrop()
                    .into(((MyViewHolder) holder).pic);
            //end glide


            myViewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, New_Category.class);
                        if (Mode.equals("activity_product")) {
                            intent = new Intent(context, New_Product.class);
                            intent.putExtra("name", changeNumber(NameItems.get(position)));
                            intent.putExtra("id", IdItems.get(position));
                            intent.putExtra("description", DescriptionItems.get(position));
                            intent.putExtra("main_image", ImageItems.get(position));
                            intent.putExtra("category_id", CategoryItems.get(position));
                            intent.putExtra("isAvailable", AvaiableItems.get(position));


                            SharedPreferences pic_writer = context.getSharedPreferences("picture", 0);
                            SharedPreferences.Editor edit = pic_writer.edit();
                            edit.clear();
                            edit.putString("Editable?", "yes");
                            edit.putString("id", IdItems.get(position));
                            edit.apply();

                        } else if (Mode.equals("activity_category")) {
                            intent = new Intent(context, New_Category.class);
                            intent.putExtra("name", changeNumber(NameItems.get(position)));
                            intent.putExtra("id", IdItems.get(position));
                            intent.putExtra("image", link);
                            SharedPreferences pic_writer = context.getSharedPreferences("picture", 0);
                            SharedPreferences.Editor edit = pic_writer.edit();
                            edit.clear();
                            edit.putString("Editable?", "yes");
                            edit.putString("id", IdItems.get(position));
                            edit.apply();
                        } else if (Mode.equals("middle_category")) {
                            intent = new Intent(context, New_MiddleCategory.class);
                            intent.putExtra("name", changeNumber(NameItems.get(position)));
                            intent.putExtra("id", IdItems.get(position));
                            intent.putExtra("image", link);
                            intent.putExtra("parent_id", ParentItems.get(position));

                            SharedPreferences pic_writer = context.getSharedPreferences("picture", 0);
                            SharedPreferences.Editor edit = pic_writer.edit();
                            edit.clear();
                            edit.putString("Editable?", "yes");
                            edit.putString("id", IdItems.get(position));
                            edit.apply();
                        } else if (Mode.equals("middle_category2")) {
                            intent = new Intent(context, New_MiddleCategory2.class);
                            intent.putExtra("name", changeNumber(NameItems.get(position)));
                            intent.putExtra("id", IdItems.get(position));
                            intent.putExtra("image", link);
                            intent.putExtra("parent_id", ParentItems.get(position));

                            SharedPreferences pic_writer = context.getSharedPreferences("picture", 0);
                            SharedPreferences.Editor edit = pic_writer.edit();
                            edit.clear();
                            edit.putString("Editable?", "yes");
                            edit.putString("id", IdItems.get(position));
                            edit.apply();
                        }

                        Log.i("mohsenjamali", "onClick: " + Mode);
                        intent.putExtra("is_edit", "true");


                        context.startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(context, "خطایی پیش آمده است لطفا دوباره امتحان کنید", Toast.LENGTH_SHORT).show();
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
                    if (Mode.equals("activity_product")) {
                        danger.setVisibility(View.GONE);

                    } else if (Mode.equals("activity_category")) {
                        danger.setVisibility(View.VISIBLE);
                    }
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

                            if (Mode.equals("activity_product")) {
                                volley.connect(context, "activity_product", "delete", false, "", "", "", IdItems.get(position), "");

                            } else if (Mode.equals("activity_category")) {
                                volley.connect(context, "activity_category", "delete", false, "", "", "", IdItems.get(position), "");
                            } else if (Mode.equals("middle_category")) {
                                volley.connect(context, "activity_category", "delete", true, "", "", "", IdItems.get(position), "1");

                            }


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
//tedad itemhara baraye nemayesh midahim
//goftim agar be entehaye list , meghdare null residim 0 bargardon vagar na tedade itemharo bargardon
        return IdItems == null ? 0 : IdItems.size();
    }


    //tarife holderhaye recyclerview
// ma chondota layout toye recyclerview darim pad bayad vase dotash holder tarif konim
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

    //-----------------in method ro ham tarif mikonim--------
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    //-----inja ham moshakas mikonim le load etelaatw jadid bayad anjam beshe ya kheir
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
