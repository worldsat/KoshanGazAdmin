package ir.koshangas.pasargad;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.text.DecimalFormat;
import java.util.List;


public class RVAdapterListName extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<String> NameItems, MobileItems, AddressItems, CityItems, TellItems, StateItems;
    private List<String> IdItems;



    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    //tarife interface
    private OnLoadMoreListener mOnLoadMoreListener;
    //----------------
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private RecyclerView recyclerView;
    private TextView tryagain_txt;
    private Button tryagain_btn;
    private ProgressBar progressBarOne;
    private Context context;
    private String RadioCustomer = "3";
    private RecyclerView mRecyclerViewlist;
    private DecimalFormat formatter = new DecimalFormat("###,###,###,###");



    public RVAdapterListName(final Context context, List<String> NameItems, List<String> MobileItems, List<String> AddressItems, List<String> IdItems, List<String> CityItems, List<String> StateItems, List<String> TellItems, RecyclerView recyclerViewlist) {
        this.context = context;
        this.MobileItems = MobileItems;
        this.AddressItems = AddressItems;
        this.CityItems = CityItems;
        this.TellItems = TellItems;
        this.StateItems = StateItems;
        this.NameItems = NameItems;
        this.IdItems = IdItems;
        this.mRecyclerViewlist = recyclerViewlist;


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
            View view = LayoutInflater.from(context).inflate(R.layout.itemview_layout_customer, parent, false);
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

            myViewHolder.name.setText(NameItems.get(position));
            myViewHolder.address.setText(AddressItems.get(position));
            myViewHolder.mobile.setText(MobileItems.get(position));
            myViewHolder.state.setText(StateItems.get(position));
            myViewHolder.city.setText(CityItems.get(position));
            myViewHolder.tell.setText(TellItems.get(position));

            // glide

          /*  final String link = "http://ghomashe.com" + ImageItems.get(position);

            Glide.with(context)
                    .load(link)
                    .override(300, 300)
                    .error(R.mipmap.no_picture)
                    .placeholder(R.mipmap.no_picture)
                    .centerCrop()
                    .into(((MyViewHolder) holder).pic);
            //end glide

*/

            myViewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    final MaterialDialog edit_alert = new MaterialDialog.Builder(context)
//                            .customView(R.layout.alert_customer, false)
//                            .autoDismiss(false)
//                            .show();
//                    RadioButton edit_alert_3 = (RadioButton) edit_alert.findViewById(R.id.radio3);
//                    RadioButton edit_alert_2 = (RadioButton) edit_alert.findViewById(R.id.radio2);
//                    RadioButton edit_alert_1 = (RadioButton) edit_alert.findViewById(R.id.radio1);
//                    Button send = (Button) edit_alert.findViewById(R.id.send);
//
//
//                    //  get Radio Value
//                    final RadioGroup payment_radio = (RadioGroup) edit_alert.findViewById(R.id.RadioGroup1);
//                    payment_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//                        @Override
//                        public void onCheckedChanged(RadioGroup arg0, int arg1) {
//                            // TODO Auto-generated method stub
//                            switch (payment_radio.getCheckedRadioButtonId()) {
//                                case R.id.radio1:
//
//                                    RadioCustomer = "1";
//                                    break;
//                                case R.id.radio2:
//
//                                    RadioCustomer = "2";
//                                    break;
//                                case R.id.radio3:
//
//                                    RadioCustomer = "3";
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    });


//                    send.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                            DateVolley volley = new DateVolley();
//
//                            volley.listName(context, "update", "update", "", "", RadioCustomer, IdItems.get(position), recyclerView, tryagain_txt, tryagain_btn, progressBarOne);
//                            edit_alert.dismiss();
//
//                        }
//                    });
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

                            volley.listName(context, "delete", "delete", "", "", "", IdItems.get(position), recyclerView, tryagain_txt, tryagain_btn, progressBarOne);


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

        return IdItems == null ? 0 : IdItems.size();
    }


    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        private LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {


        TextView name, edit, delete, address, mobile, tell, state, city,  date;

        private MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            mobile = itemView.findViewById(R.id.mobile);
            tell = itemView.findViewById(R.id.tell);
            state = itemView.findViewById(R.id.state);
            city = itemView.findViewById(R.id.city);
            edit = itemView.findViewById(R.id.edit);
            date = itemView.findViewById(R.id.date);
            delete = itemView.findViewById(R.id.delete);


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
