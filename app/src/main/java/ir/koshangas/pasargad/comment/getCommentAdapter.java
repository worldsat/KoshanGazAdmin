package ir.koshangas.pasargad.comment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ir.koshangas.pasargad.MiladiToShamsi;
import ir.koshangas.pasargad.OnLoadMoreListener;
import ir.koshangas.pasargad.R;


public class getCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<String> VisibleItems, IdItems, TextItems, NameProductItems, DateItems, MobileItems;


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;
    //----------------
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private Context context;

    private RecyclerView mRecyclerViewlist;


    public getCommentAdapter(final Context context, List<String> MobileItems, List<String> NameProductItems, List<String> VisibleItems, List<String> IdItems, List<String> TextItems, List<String> DateItems, RecyclerView recyclerViewlist) {

        this.context = context;
        this.VisibleItems = VisibleItems;
        this.IdItems = IdItems;
        this.DateItems = DateItems;
        this.mRecyclerViewlist = recyclerViewlist;
        this.TextItems = TextItems;
        this.NameProductItems = NameProductItems;
        this.MobileItems = MobileItems;


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
        return IdItems.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    //--------------------------------------------------------MyViewHolder----------------------------------------------
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view;

            view = LayoutInflater.from(context).inflate(R.layout.itemview_layout_comment, parent, false);

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

            MiladiToShamsi miladiToShamsi = new MiladiToShamsi();


            myViewHolder.Text.setText(changeNumber(TextItems.get(position)));
            myViewHolder.Name.setText(changeNumber(NameProductItems.get(position)));
            myViewHolder.date.setText(miladiToShamsi.convert(DateItems.get(position)));
            myViewHolder.mobile.setText(MobileItems.get(position));

            if (VisibleItems.get(position).equals("0")) {
                myViewHolder.visible.setText("نمایش نظر");
            } else if (VisibleItems.get(position).equals("1")) {
                myViewHolder.visible.setText("عدم نمایش نظر");
            }

            myViewHolder.visible.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UpdateComment updateComment = new UpdateComment();
                    updateComment.Update(context, IdItems.get(position), VisibleItems.get(position));

                }
            });


            myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DeleteComment deleteComment = new DeleteComment();
                    deleteComment.DeleteItem(context, IdItems.get(position));

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


        TextView Name, Text, date, mobile;
        TextView visible, delete;

        private MyViewHolder(View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.name);
            Text = itemView.findViewById(R.id.comment);
            date = itemView.findViewById(R.id.date);
            visible = itemView.findViewById(R.id.visiblity);
            delete = itemView.findViewById(R.id.delete);
            mobile = itemView.findViewById(R.id.mobile);

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
